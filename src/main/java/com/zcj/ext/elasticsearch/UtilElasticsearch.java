package com.zcj.ext.elasticsearch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.zcj.util.UtilString;

public class UtilElasticsearch {

	private static Logger logger = LoggerFactory.getLogger(UtilElasticsearch.class);

	/**
	 * 搜索条件：日期区间
	 * 
	 * @param field
	 *            字段名称
	 * @param begin
	 *            开始时间
	 * @param end
	 *            截止时间
	 * @param mappingDateFormat
	 *            ES的mappings中日期的格式
	 *            <p>
	 *            NULL 表示默认（strict_date_optional_time || epoch_millis）
	 *            <p>
	 *            例： {@code yyyy-MM-dd HH:mm:ss}
	 * 
	 * @return
	 */
	public static RangeQueryBuilder queryBuilderByRange(String field, Date begin, Date end, String mappingDateFormat) {
		if (UtilString.isBlank(field) || (begin == null && end == null)) {
			return null;
		}
		RangeQueryBuilder timeFilter = QueryBuilders.rangeQuery(field);
		if (UtilString.isNotBlank(mappingDateFormat)) {
			SimpleDateFormat format = new SimpleDateFormat(mappingDateFormat);
			if (begin != null) {
				timeFilter.from(format.format(begin)).includeLower(true);
			}
			if (end != null) {
				timeFilter.to(format.format(end)).includeUpper(true);
			}
		} else {
			if (begin != null) {
				timeFilter.from(begin).includeLower(true);
			}
			if (end != null) {
				timeFilter.to(end).includeUpper(true);
			}
		}
		return timeFilter;
	}

	/** 搜索条件：坐标矩形框 */
	public static GeoBoundingBoxQueryBuilder queryBuilderByGeoBound(String field, Double lngBegin, Double lngEnd,
			Double latBegin, Double latEnd) {
		if (UtilString.isBlank(field) || lngBegin == null || lngEnd == null || latBegin == null || latEnd == null) {
			return null;
		}
		GeoBoundingBoxQueryBuilder queryBuilder = QueryBuilders.geoBoundingBoxQuery(field)
				.bottomRight(latBegin, lngEnd).topLeft(latEnd, lngBegin)
				// 默认是memory；indexed表示明确告诉ES使用经度和纬度的倒排索引（Mapping中字段配置了"lat_lon":true才有效）
				.type("indexed");
		return queryBuilder;
	}

	/** 搜索条件：坐标圆圈 */
	public static GeoDistanceQueryBuilder queryBuilderByGeoDistance(String field, Double centerLng, Double centerLat,
			Double distanceMeters) {
		if (UtilString.isBlank(field) || centerLng == null || centerLat == null || distanceMeters == null) {
			return null;
		}
		GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery(field).point(centerLat, centerLng)
				.distance(distanceMeters, DistanceUnit.METERS);
		return queryBuilder;
	}

	/** 搜索条件：表达式 */
	public static QueryStringQueryBuilder queryBuilderByString(String val) {
		if (UtilString.isBlank(val)) {
			return null;
		}
		return QueryBuilders.queryStringQuery(val);
	}

	/** 合并多个查询条件（Query模式） */
	public static QueryBuilder bindQueryBuilder(QueryBuilder... builder) {
		if (builder == null || builder.length == 0) {
			return null;
		}
		List<QueryBuilder> noNull = new ArrayList<QueryBuilder>();
		for (QueryBuilder b : builder) {
			if (b != null) {
				noNull.add(b);
			}
		}
		if (noNull.size() == 0) {
			return null;
		} else if (noNull.size() == 1) {
			return noNull.get(0);
		} else {
			BoolQueryBuilder filter = new BoolQueryBuilder();
			for (QueryBuilder bb : noNull) {
				filter.must(bb);
			}
			return filter;
		}
	}

	/** 合并多个查询条件（QueryFilter模式） */
	public static QueryBuilder bindQueryFilterBuilder(QueryBuilder... builder) {
		if (builder == null || builder.length == 0) {
			return null;
		}
		List<QueryBuilder> noNull = new ArrayList<QueryBuilder>();
		for (QueryBuilder b : builder) {
			if (b != null) {
				noNull.add(b);
			}
		}
		if (noNull.size() == 0) {
			return null;
		} else {
			BoolQueryBuilder filter = QueryBuilders.boolQuery();
			for (QueryBuilder bb : noNull) {
				filter.filter(bb);
			}
			return filter;
		}
	}

	/** 排序方式：离中心点的距离 */
	public static SortBuilder sortBuilderByGeo(String field, Double lat, Double lng) {
		if (UtilString.isBlank(field) || lat == null || lng == null) {
			return null;
		}
		SortBuilder sortBuilder = SortBuilders.geoDistanceSort(field).point(lat, lng).unit(DistanceUnit.METERS)
				.order(SortOrder.ASC);
		return sortBuilder;
	}

	/** 判断索引的类型是否存在 */
	public static boolean isExistsType(Client client, String indexName, String indexType) {
		if (UtilString.isBlank(indexName) || UtilString.isBlank(indexType)) {
			return false;
		}
		if (!isExistsIndex(client, indexName)) {
			return false;
		}
		TypesExistsResponse response = client.admin().indices()
				.typesExists(new TypesExistsRequest(new String[] { indexName }, indexType)).actionGet();
		return response.isExists();
	}

	/** 判断索引是否存在 */
	public static boolean isExistsIndex(Client client, String indexName) {
		IndicesExistsResponse response = client.admin().indices().exists(new IndicesExistsRequest(indexName))
				.actionGet();
		return response.isExists();
	}

	/** 根据索引别名获取索引名称 */
	public static Set<String> getIndexNameByAlias(Client client, String alias) {
		Set<String> result = new HashSet<String>();
		if (client != null && UtilString.isNotBlank(alias)) {
			Iterator<ObjectCursor<String>> a = client.admin().indices().getAliases(new GetAliasesRequest(alias))
					.actionGet().getAliases().keys().iterator();
			while (a.hasNext()) {
				result.add(a.next().value);
			}
		}
		return result;
	}

	/** SearchHit[] 转 List */
	public static List<Map<String, Object>> toList(SearchHit[] hits) {
		if (hits == null || hits.length == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (SearchHit hit : hits) {
			result.add(hit.getSource());
		}
		return result;
	}

	/** 查询某字段的最大值 */
	public static Object getMaxValue(Client client, String indexName, String typeName, String fliedName) {
		try {
			if (isExistsType(client, indexName, typeName)) {
				SearchResponse response = client.prepareSearch(indexName).setTypes(typeName)
						.addSort(fliedName, SortOrder.DESC).setSize(1).execute().actionGet();
				return response.getHits().getHits()[0].getSource().get(fliedName);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/** 查询某字段的最小值 */
	public static Object getMinValue(Client client, String indexName, String typeName, String fliedName) {
		try {
			if (isExistsType(client, indexName, typeName)) {
				SearchResponse response = client.prepareSearch(indexName).setTypes(typeName)
						.addSort(fliedName, SortOrder.ASC).setSize(1).execute().actionGet();
				return response.getHits().getHits()[0].getSource().get(fliedName);
			}
		} catch (Exception e) {
		}
		return null;
	}

	/** 查询总记录数 */
	public static long count(Client client, String indexName, String typeName) {
		try {
			SearchResponse response = client.prepareSearch(indexName).setTypes(typeName).execute().actionGet();
			return response.getHits().getTotalHits();
		} catch (Exception e) {
		}
		return 0L;
	}

	/** 查询所有索引的记录总数 */
	public static long count(Client client) {
		try {
			SearchResponse response = client.prepareSearch("_all").execute().actionGet();
			return response.getHits().getTotalHits();
		} catch (Exception e) {
		}
		return 0L;
	}

}
