package test.zcj.ext.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregator.SubAggCollectionMode;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Order;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanksBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zcj.ext.elasticsearch.UtilElasticsearch;
import com.zcj.ext.elasticsearch.UtilElasticsearchExt;
import com.zcj.web.dto.ServiceResult;

public class TestElasticsearch {

	private static Client client;

	@BeforeClass
	public static void init() {
		try {
			Settings settings = Settings.settingsBuilder()
					.put("cluster.name", "ohgayun")
					.put("client.transport.sniff", true)
					.build();
			client = TransportClient.builder().settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.119"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void close() {
		// client.close();
	}
	
	// 单条插入数据
	@Test
	public void insert() throws IOException {
		// XcontentBuilder方式构建JSON、自定义ID插入
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
					.field("user", "kimchy")
					.field("postDate", new Date())
					.field("es_point", new double[] { 120.0001, 27.0001 })
				.endObject();
		IndexResponse response = client.prepareIndex("twitter", "tweet", "1").setSource(builder).get();
		System.out.println(response.toString());

		// String方式构建JSON、自动生成ID插入
		String json = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\"," + "\"message\":\"trying out Elasticsearch\"" + "}";
		IndexResponse response2 = client.prepareIndex("twitter", "tweet").setSource(json).get();
		System.out.println(response2.toString());
	}

	// 批量插入数据
	@Test
	public void inserts() {
		final String indexName = "twitter";
		final String typeName = "tweet";
		BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
				if (!UtilElasticsearch.isExistsIndex(client, indexName)) {
					UtilElasticsearchExt.createDefaultMapping(client, indexName);
				}
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				if (response.hasFailures()) {
					System.out.println("保存数据到ES失败：" + response.buildFailureMessage());
				} else {
					System.out.println("保存数据到ES成功");
				}
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				System.out.println("保存数据到ES失败：" + failure.getMessage());
			}
		})
		.setBulkActions(10000)	// 消息数量到达10000
		// .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))	// 消息大小达到5M
		// .setFlushInterval(TimeValue.timeValueSeconds(5))	// 时间达到5s
		.setConcurrentRequests(1)	// 设置允许并发请求的数量
		.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))	// 设置请求失败时的补偿措施，重复请求3次
		.build();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "zzz");
		map.put("ctime", "2013-01-30 12:15:25");
		map.put(UtilElasticsearchExt.GEO_FIELD_NAME, new Double[] {120.579813, 27.938374});
		String jsonString = ServiceResult.GSON_DT.toJson(map);
		bulkProcessor.add(new IndexRequest(indexName, typeName).source(jsonString));
		bulkProcessor.add(new IndexRequest(indexName, typeName, "2").source(jsonString));
		bulkProcessor.flush();
	}
	
	// 新建索引（同时设置Type的mapping）
	@Test
	public void createMapping() throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.startObject("properties")
					.startObject("id").field("type", "long").field("index", "no").endObject()
					.startObject("name").field("type", "string").field("index", "analyzed").field("analyzer", "standard").endObject()
					.startObject("idcard").field("type", "string").field("index", "analyzed").field("analyzer", "standard").endObject()
					.startObject("address").field("type", "string").field("index", "analyzed").field("analyzer", "standard").endObject()
					.startObject("tel").field("type", "string").field("index", "analyzed").field("analyzer", "standard").endObject()
					.startObject("hospital").field("type", "string").field("index", "not_analyzed").endObject()
					.startObject("ks").field("type", "string").field("index", "not_analyzed").endObject()
					.startObject("jztime").field("type", "string").field("index", "not_analyzed").endObject()
					.startObject("uptime").field("type", "string").field("index", "not_analyzed").endObject()
					.startObject("flag").field("type", "string").field("index", "no").endObject()
				.endObject()
				.endObject();
		client.admin().indices().prepareCreate("szxt2")
		// 设置分片数
		.setSettings(Settings.settingsBuilder().put("number_of_shards", 10).build())
		.addMapping("wsxt2", builder)
		.execute().actionGet();
		System.out.println("创建成功");
	}
	
	// 新建索引（同时设置_default_：配置默认日期格式、默认坐标格式）
	@Test
	public void createDefaultMapping() throws IOException {
		UtilElasticsearchExt.createDefaultMapping(client, "wsxt2");
		System.out.println("创建成功");
	}
	
	// 删除索引
	@Test
	public void deleteIndex() {
		String indexName = "v3_apmsg";
		if (UtilElasticsearch.isExistsIndex(client, indexName)) {
			DeleteIndexResponse response = client.admin().indices().prepareDelete(indexName).execute().actionGet();
			System.out.println(response.isAcknowledged());
		} else {
			System.out.println("索引不存在");
		}
	}

	// 查询最大值
	@Test
	public void findMax() {
		Object id = UtilElasticsearch.getMaxValue(client, "szxt", "wsxt", "id");
		System.out.println(id.toString());
	}

	// 查询总量
	@Test
	public void findCount() {
		long count = UtilElasticsearch.count(client, "szxt", "wsxt");
		System.out.println("szxt-wsxt的记录数：" + count);
		long allCount = UtilElasticsearch.count(client);
		System.out.println("整个ES库的总记录数：" + allCount);
	}

	// 根据ID查询数据对象
	@Test
	public void findById() {
		// 查询：根据ID查询数据
		GetResponse response = client.prepareGet("szxt", "wsxt", "AVZOF87_eFgZvRT4AyEA").get();
		System.out.println(response.getSourceAsString());

		// 查询：根据ID查询数据
		GetResponse response2 = client.prepareGet("szxt", "wsxt", "AVZOF87_eFgZvRT4AyEA").setOperationThreaded(false)
				.get();
		System.out.println(response2.getSourceAsString());
	}

	// 根据条件查询
	@SuppressWarnings("deprecation")
	@Test
	public void find() {
		SearchResponse response = client.prepareSearch("szxt").setTypes("wsxt")
				.addSort("id", SortOrder.ASC)
				.setScroll(new TimeValue(60000))
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.filteredQuery(
						QueryBuilders.matchAllQuery(),							// query：查询：相关性查询、评分（全文搜索、评分排序，使用query）
						QueryBuilders.termQuery("essid", "TP-LINK_292A")  		// filter：过滤：查询时就过滤、精确查询、缓存结果（是非过滤，精确匹配，使用filter）
				))
				.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))	// post_filter：聚合后过滤；查询之后执行；只与聚合一起使用
				.setFrom(0).setSize(60)
				.setExplain(true)
				.execute().actionGet();
		System.out.println("搜索耗时：" + response.getTookInMillis() + "毫秒；总数：" + response.getHits().getTotalHits());
		for (SearchHit hit : response.getHits().getHits()) {
			System.out.println(hit.getSourceAsString());
		}
	}
	
	// 根据条件分页查询数据（时间段、经纬度矩形）
	@Test
	public void findByKey() {
		SearchResponse result = UtilElasticsearchExt.search(client, "nanxj", "tbl_express", null, null, "李碎华 13780136807 温州市",
				true, null, null, null, null, null, null, 0, 2);
		if (result != null) {
			System.out.println(UtilElasticsearch.toList(result.getHits().getHits()));
		}
	}

	// 构建搜索条件
	@Test
	public void initQuery() {
		
		// match_all：匹配所有
		QueryBuilder qb = QueryBuilders.matchAllQuery();
		System.out.println(qb);

		qb = QueryBuilders.queryStringQuery("\"邹邹\"").field("name")
				.defaultOperator(QueryStringQueryBuilder.Operator.AND);
		System.out.println(qb);

		// 表达式查询
		// 1[短语查询]：+\"某某某\" ：分词后按顺序匹配每个分词，两个词中间不能隔开其他词
		// +\"中心卫\" 匹配：XXX中心卫XXX 不匹配：XXX中X心卫XX （分词后：中、心、卫）
		// +\"1378\" 匹配：1378、某13780某 不匹配：137801 （分词后：1378）
		// 2[通配符查询]：+*瓯 第* ：去掉空格后面的内容-->分词-->如果大于1个词-->都不匹配-->如果只有一个词-->"%词%"方式匹配
		// +*瓯 第* 匹配：XX瓯XX
		// +*瓯第* 不匹配：瓯第、XX瓯第XX
		// +*1378* 匹配：XX1378XX
		qb = QueryBuilders.queryStringQuery("+\"13780136807\" +*ccc* +realname:eee +realname:\"邹\" -abc");
		System.out.println(qb.toString());

		// (name LIKE '%mary%' OR name LIKE '%john%') AND (date > '2014-09-10') AND (_all LIKE '%abc%' OR _all LIKE '%ddd%')
		qb = QueryBuilders.queryStringQuery("+name:(mary john) +date:>2014-09-10 +(abc ddd)");
		System.out.println(qb.toString());
		
		// exist：某字段有值
		qb = QueryBuilders.existsQuery("address");
		System.out.println(qb.toString());
		
		// match
		// Field为_all、analyzed字符串时：Field内容分词 + Key内容分词 + 全文检索
		// Field为long、date、boolean、not_analyzed字符串时：精确匹配
		qb = QueryBuilders.matchQuery("_all", "aaa bbb")
				.operator(MatchQueryBuilder.Operator.AND);// 分词后多个词用AND拼接（默认是OR）
		System.out.println(qb.toString());
		
		// multi_match：match可指定多个Field
		qb = QueryBuilders.multiMatchQuery("abc ddd", "realname", "firstname");
		System.out.println(qb.toString());
		
		// match_phrase：短语匹配；+\"aaa ccc\"；分词后按顺序匹配每个分词，两个词中间不能隔开其他词；性能比match差
		qb = QueryBuilders.matchPhraseQuery("_all", "aaa ccc");
		System.out.println(qb.toString());
		
		// prefix：前缀匹配：一个个词匹配
		qb = QueryBuilders.prefixQuery("phone", "137801");
		System.out.println(qb.toString());
		
		// phrase_prefix：短语匹配 + 最后一个前缀匹配；常用于输入提醒
		qb = QueryBuilders.matchPhrasePrefixQuery("phone", "aaa ccc 1378");
		System.out.println(qb.toString());
		
		// wildcard：通配符查询：一个个词匹配
		qb = QueryBuilders.wildcardQuery("phone", "1?78*6807");
		System.out.println(qb);
		
		// regexp：正则表达式查询：一个个词匹配
		qb = QueryBuilders.regexpQuery("phone", "137[0-9].+");
		System.out.println(qb);
		
		// term：Field内容分词后的数组 包含 Key内容[Key不分词：如果要精确查询，输入的内容一定要先分词处理或转小写]
		qb = QueryBuilders.termQuery("_all", "白杨");
		System.out.println(qb.toString());
		
		// terms：term OR term
		qb = QueryBuilders.termsQuery("_all", "邹", "李");
		System.out.println(qb.toString());
		
		// term+constant_score[不评分]：将term查询转化成为filter（不评分，分值都是1）
		qb = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("essid", "TP-LINK_292A"));
		System.out.println(qb.toString());
		
		// bool.filter：类似于constant_score，不过分值都是0
		qb = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("essid", "TP-LINK_292A"));
		System.out.println(qb.toString());
		
		// range：范围查询
		qb = QueryBuilders.rangeQuery("ctime").from(new Date()).to(new Date());
		System.out.println(qb.toString());
		
		// bool(must=and、must_not=not、should=or)
		// 更详细的过滤条件优先放在前面
		qb = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("content", "test1"))
				.mustNot(QueryBuilders.termQuery("content", "test2"))
				.should(QueryBuilders.termQuery("content", "test3"));
		System.out.println(qb.toString());
		
		// geo_distance
		qb = UtilElasticsearch.queryBuilderByGeoDistance("myPoint", 27.152515, 125.548485, 1000d);
		System.out.println(qb.toString());

		// geo_bbox
		qb = UtilElasticsearch.queryBuilderByGeoBound("myPoint", 27.152515, 28.152545, 125.518454, 129.8548484);
		System.out.println(qb.toString());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void aggregationsTest() {
		// 查询条件
		QueryBuilder query = QueryBuilders.matchAllQuery();

		// terms:聚合
		TermsBuilder aggs = AggregationBuilders.terms("myaggs").field("tel").size(5);
		
		// terms:聚合+度量
		TermsBuilder aggs2 = AggregationBuilders.terms("myaggs").field("tel").size(5)
				// avg[取平均值]、max[取最大值]、min[取最小值]、sum[总和]
				.subAggregation(AggregationBuilders.max("myaggs2").field("id"));
		
		// terms:聚合+度量+嵌套聚合
		TermsBuilder aggs3 = AggregationBuilders.terms("myaggs").field("tel").size(5)
				// 深度优先[默认]：1、构建完整的多级数 2、修剪无用节点
				// 广度优先[BREADTH_FIRST]：1、构建第一层数[附带所有第二层的数据] 2、修剪无用节点 3、构建第二层数 ；
				// 一般情况下使用深度优先，如按月份聚合；广度优先用于第二层各数量远远小于第一层总组数的情况下
				.collectMode(SubAggCollectionMode.BREADTH_FIRST)
				.subAggregation(AggregationBuilders.max("myaggs2").field("id"))
				.subAggregation(AggregationBuilders.terms("myaggs3").field("idcard").size(3));
		
		// histogram:按区间分组+度量
		HistogramBuilder aggs4 = AggregationBuilders.histogram("myaggs").field("id")
				.interval(10170000)
				// _count[按文档匹配总数排序]
				// _term[按值排序，用于terms]、
				// _key[按值排序，用于histogram和date_histogram]
				// myaggsname[按指定metric结果排序]
				.order(Order.COUNT_ASC)
				.subAggregation(AggregationBuilders.max("myaggs2").field("id"));
		
		// date_histogram:按时段分组
		DateHistogramBuilder aggs5 = AggregationBuilders.dateHistogram("myaggs").field("recivedate")
				.interval(DateHistogramInterval.DAY)
				// .interval(DateHistogramInterval.hours(3))	// 3小时一个区间
				.format("yyyy-MM-dd")
				// .timeZone("Asia/Shanghai")	// 设置时区，会影响分组查询
				// .format("HH")	// key_as_string内容显示为小时数，如：06
				// .offset("+6h")	// 偏移6小时开始分组，按日分组统计时可做到06:00算每一天的开始
				.minDocCount(1)		// 数量达到多少时显示出来（0表示中间组无数据也显示）
				.extendedBounds("2015-03-01", "2015-03-11");	// 强制显示时间区间的数据，无数据也显示出来
		
		// cardinality:统计去重后的数量:COUNT(DISTINCT hospical)
		CardinalityBuilder aggs6 = AggregationBuilders
				.cardinality("myaggs")
				.field("hospital")
				// 配置精确基数，最大值为40000；1000表示1000个内非常精确；100 的阈值可以在唯一值为百万的情况下仍然将误差维持 5% 以内
				.precisionThreshold(100);
		
		// percentiles:升序、按百分比分组后取得的指定百分比下的文档平均值
		PercentilesBuilder aggs7 = AggregationBuilders
				.percentiles("myaggs")
				.field("pwr")
				.percentiles(5, 10, 50, 99);	// 自定义百分比，返回的是落在某个百分比以下的所有文档的最小值
		
		// percentile_ranks:升序、计算有多少比例的数据在指定的值以内
		PercentileRanksBuilder aggs8 = AggregationBuilders
				.percentileRanks("myapps")
				.field("pwr")
				.percentiles(-85, -70);
		
		SearchRequestBuilder builder = client.prepareSearch("vw_wifi_device").setTypes("vw_wifi_device")
				.setQuery(query)
				.setSize(0)	// 设置为0可以省略Hits内容提高效率
				.addAggregation(aggs5);
		System.out.println(builder.toString());
		
		// 执行查询
		SearchResponse response = builder.execute().actionGet();
		System.out.println(response.toString());
		
		// 转换格式
//		List<Bucket> blist = ((StringTerms) response.getAggregations().asMap().get("myaggs")).getBuckets();
//		double blist2 = ((InternalMax) blist.get(0).getAggregations().asMap().get("myaggs2")).getValue();
//		List<? extends Histogram.Bucket> blist2 = ((Histogram) response.getAggregations().asMap().get("myaggs")).getBuckets();
	}

	// 对关键字进行分词，并转化成Query字符串和PostFilter字符串
	@Test
	public void initAnalyze() {
		String val = "123邹z1223f烦 13780136807 烦烦 355854ad5d59 330381198811053411 zouchongjin";
		String[] qf = UtilElasticsearchExt.initQueryAndFilter(client, val);
		System.out.println(qf[0]);
		System.out.println(qf[1]);
	}
	
	@Test
	public void aliases() {
//		client.admin().indices().prepareAliases()
//		.removeAlias("v3_mobileessid_20180418", "fsdfd")
//		.addAlias("v3_mobileconap_20180419", "fsdfd")
//		.execute();

		Set<String> indexNames = UtilElasticsearch.getIndexNameByAlias(client, "v3_msg");
		System.out.println(indexNames);
	}

}
