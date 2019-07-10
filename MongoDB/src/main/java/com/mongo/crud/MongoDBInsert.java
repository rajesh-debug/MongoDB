package com.mongo.crud;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBInsert {

	public static void main(String[] args) {

		/** Create connection with MongoClient. */
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
			
			/** Open database. */
			MongoDatabase db = mongo.getDatabase("testdb");

			MongoCollection<Document> col = db.getCollection("Persons");

			List<Document> docs = new ArrayList<Document>();
			docs.add(new Document("name", "Awadh"));
			docs.add(new Document("name", "Vinita").append("age", 25));

			col.insertMany(docs);

			for (Document doc : docs) {
				System.out.println("Name=" + doc.getString("name") + ", age=" + doc.getInteger("age", 0) + ", ID="
						+ doc.getObjectId("_id").toString());
			}
		}
	}
}
