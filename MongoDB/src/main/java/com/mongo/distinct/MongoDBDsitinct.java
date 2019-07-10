package com.mongo.distinct;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Sort the collection by name in descending order.
 * collection.find().sort(Filters.eq("name", -1))
 * 
 * @author rajesh.dixit
 */
public class MongoDBDsitinct {

	public static void main(String[] args) {

		/** Create connection with MongoClient. */
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {

			/** Open database. */
			MongoDatabase db = mongo.getDatabase("testdb");

			MongoCollection<Document> collection = db.getCollection("Persons");

			// Delete all documents from collection.
			collection.deleteMany(Filters.or(Filters.eq("name", "Awadh"), Filters.eq("age", 25)));

			List<Document> docs = new ArrayList<Document>();
			docs.add(new Document("name", "Awadh"));
			docs.add(new Document("name", "Vinita").append("age", 25));
			docs.add(new Document("name", "Awadh").append("age", 30));

			// insert multiple records in mongo db.
			collection.insertMany(docs);

			// Find the distinct name in the collection.
			DistinctIterable<String> findIterable = collection.distinct("name", String.class);

			// Fetch all documents from the collection.
			try (MongoCursor<String> cursor = findIterable.iterator()) {
				while (cursor.hasNext()) {
					System.out.println(cursor.next());
				}
			}
		}
	}
}