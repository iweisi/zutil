package test.zcj.ext.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.WriteModel;

public class TestMongoDB {

	private static MongoDatabase db;
	private static MongoClient client;
	private static MongoCollection<Document> collection;

	@BeforeClass
	public static void init() {
		client = new MongoClient("localhost", 27017);
		db = client.getDatabase("testdb");
		collection = db.getCollection("testcol");
	}

	@Test
	public void t1() {

		// 查询一条
		Document myDoc = collection.find().first();
		System.out.println(myDoc.toJson());

		// 查询
		List<Document> res = collection.find().projection(Projections.excludeId()).sort(Sorts.descending("_id")).skip(0).limit(2)
				.into(new ArrayList<Document>());
		System.out.println(res);

		Block<Document> printBlock = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				System.out.println(document.toJson());
			}
		};
		// 查询并循环查询结果
		collection.find(Filters.gt("cou", 2)).forEach(printBlock);

		// 查询总数
		long count = collection.count();
		System.out.println(count);
	}

	@Test
	public void t2() {
		Document doc = new Document("name", "abc").append("cou", 5).append("info", new Document("x", 203).append("y", 102));
		// 插入
		collection.insertOne(doc);

		Document myDoc = collection.find().first();
		// 更新
		collection.updateOne(Filters.eq("_id", myDoc.getObjectId("_id")), new Document("$set", new Document("cou", 6)));
	}

	@Test
	public void t4() {
		List<WriteModel<Document>> modelList = new ArrayList<WriteModel<Document>>(10000);
		for (int i = 0; i < 10000; i++) {
			modelList.add(new InsertOneModel<Document>(new Document("name", "name" + i)));
		}

		// 批量执行
		collection.bulkWrite(modelList);

		// 批量执行（不按顺序执行，速度快）
		collection.bulkWrite(modelList, new BulkWriteOptions().ordered(false));
	}

	@Test
	public void t5() {
		List<Document> modelList = new ArrayList<Document>(10000);
		for (int i = 0; i < 10000; i++) {
			modelList.add(new Document("name", "name" + i));
		}

		// 批量插入
		collection.insertMany(modelList);
	}

	@Test
	public void t6() {
		// 查询现有索引
		List<String> curIndexName = new ArrayList<String>();
		ListIndexesIterable<Document> a = collection.listIndexes();
		for (MongoCursor<Document> iterator = a.iterator(); iterator.hasNext();) {
			Document doc = iterator.next();
			curIndexName.add(doc.getString("name"));
		}

		// 如果索引不存在，则创建
		if (!curIndexName.contains("index_testcol_name")) {
			collection.createIndex(Indexes.ascending("name"), new IndexOptions().name("index_testcol_name").background(true));
		}
	}

	@Test
	public void t7() {
		// Document doc = new Document().append("zpoint",
		// Arrays.asList(108.256585, 28.565854));
		// collection.insertOne(doc);
		//
		// // 创建经纬度索引
		// collection.createIndex(Indexes.geo2d("zpoint"), new
		// IndexOptions().name("index_testcol_zpoint").background(true));
		//
		// // 根据矩形查询
		// collection.find(Filters.geoWithinBox("zpoint", 107.000001, 27.000001,
		// 109.000001, 29.000001)).forEach(printBlock);

		// 根据圆形查询
		collection.find(Filters.geoWithinCenter("zpoint", 108.000001, 28.000001, 1)).forEach(printBlock);
	}

	@Test
	public void t8() {

		// 获取表名
		System.out.println(collection.getNamespace().getCollectionName());
	}

	@Test
	public void t9() {

	}

	Block<Document> printBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {
			System.out.println(document.toJson());
		}
	};

}
