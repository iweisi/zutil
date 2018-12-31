package com.zcj.ext.elasticsearch;

import java.util.Date;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcj.util.UtilString;

public class UtilElasticsearchExt {

	private static Logger logger = LoggerFactory.getLogger(UtilElasticsearchExt.class);
	
	public static final String GEO_FIELD_NAME = "es_point";
	
	/** 新建索引（同时设置_default_：配置默认日期格式、默认坐标格式）*/
	public static boolean createDefaultMapping(Client client, String indexName) {
		if (UtilElasticsearch.isExistsIndex(client, indexName)) {
			logger.error(indexName + "创建失败（已存在）");
			return false;
		}
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder()
					.startObject()
						.field("dynamic_date_formats", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" })
						.field("properties")
							.startObject()
								.startObject(GEO_FIELD_NAME).field("type", "geo_point").field("lat_lon", true).endObject()
							.endObject()
					.endObject();
			client.admin().indices().prepareCreate(indexName).addMapping("_default_", builder).execute().actionGet();
			logger.info("创建" + indexName + "成功");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public static String[] initQueryAndFilter(Client client, String val) {
		String queryString = "";
		String filterString = "";
		if (UtilString.isNotBlank(val)) {
			val = val.trim();
			String[] valArray = val.split(" ");
			for (String v : valArray) {
				if (UtilString.isPhone(v) || UtilString.isIdcard(v) || UtilString.isIp(v) || UtilString.isMAC(v)
						|| UtilString.isCarNumber(v)) {// 手机号、身份证号、IP、MAC、车牌号
					filterString += "+\"" + v + "\" ";
				} else {
					AnalyzeResponse response = client.admin().indices().prepareAnalyze(null, v).execute().actionGet();
					for (AnalyzeToken t : response.getTokens()) {
						if ("<ALPHANUM>".equals(t.getType()) || "<NUM>".equals(t.getType())) {// 字母或数字
							filterString += "+*" + t.getTerm() + "* ";
						} else if ("<IDEOGRAPHIC>".equals(t.getType())) {// 文本
							queryString += t.getTerm();
						}
					}
				}
			}
		}
		return new String[] { queryString, filterString };
	}

	/**
	 * 根据条件分页查询数据（时间段、经纬度矩形）
	 * 
	 * @param client
	 *            *
	 * @param indexName
	 *            *
	 * @param typeName
	 *            *
	 * @param timeField
	 *            按时间段搜索时的字段名称
	 * @param sortField
	 *            排序字段名称
	 * @param searchKey
	 *            查询关键字
	 * @param analysis
	 *            是否分词
	 * @param timeBegin
	 *            开始时间
	 * @param timeEnd
	 *            截止时间
	 * @param lngBegin
	 * @param lngEnd
	 * @param latBegin
	 * @param latEnd
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public static SearchResponse search(Client client, String indexName, String typeName, String timeField,
			String sortField, String searchKey, boolean analysis, Date timeBegin, Date timeEnd, Double lngBegin,
			Double lngEnd, Double latBegin, Double latEnd, int offset, int pagesize) {
		
		if (!UtilElasticsearch.isExistsType(client, indexName, typeName)) {
			return null;
		}
		
		if (offset + pagesize > 100000) {
			logger.warn("分页数过大：offset=" + offset + ";pagesize=" + pagesize);
			return null;
		}

		SearchRequestBuilder builder = client.prepareSearch(indexName).setTypes(typeName);

		QueryBuilder keyFilter = null;
		if (analysis) {
			String[] keyArray = UtilElasticsearchExt.initQueryAndFilter(client, searchKey);
			if (UtilString.isNotBlank(keyArray[0])) {
				QueryBuilder query = QueryBuilders.matchQuery("_all", keyArray[0]).operator(Operator.AND);
				builder.setQuery(query);
			}
			keyFilter = UtilElasticsearch.queryBuilderByString(keyArray[1]);
		} else {
			keyFilter = UtilElasticsearch.queryBuilderByString("\"" + searchKey.trim() + "\"");
		}
		GeoBoundingBoxQueryBuilder geoFilter = UtilElasticsearch.queryBuilderByGeoBound(
				UtilElasticsearchExt.GEO_FIELD_NAME, lngBegin, lngEnd, latBegin, latEnd);
		RangeQueryBuilder timeFilter = UtilElasticsearch.queryBuilderByRange(timeField, timeBegin, timeEnd,
				"yyyy-MM-dd HH:mm:ss");
		QueryBuilder allFilter = UtilElasticsearch.bindQueryBuilder(keyFilter, geoFilter, timeFilter);
		if (allFilter != null) {
			builder.setPostFilter(allFilter);
		}

		if (UtilString.isNotBlank(sortField)) {
			builder.addSort(sortField, SortOrder.DESC);
		}

		builder.setFrom(offset).setSize(pagesize);
		logger.debug(builder.toString());

		SearchResponse response = builder.execute().actionGet();
		logger.debug("搜索 " + indexName + " " + typeName + " 耗时：" + response.getTookInMillis() + "毫秒；总数："
				+ response.getHits().getTotalHits());

		return response;
	}
}
