package com.mongo.crud;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBFind {

	public static void main(String[] args) {
		/** Create connection with MongoClient. */
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {

			/** Open database. */
			MongoDatabase db = mongo.getDatabase("testdb");

			MongoCollection<Document> collection = db.getCollection("Persons");

			List<Document> docs = new ArrayList<Document>();
			docs.add(new Document("name", "Awadh"));
			docs.add(new Document("name", "Vinita").append("age", 25));

			// insert multiple records in mongo db.
			collection.insertMany(docs);

			// Fetch first document from the collection.
			Document firstDoc = collection.find().first();
			System.out.println("Find the first doc in collection " + firstDoc);

			// Fetch all documents from the collection.
			try (MongoCursor<Document> cursor = collection.find().iterator()) {
				while (cursor.hasNext()) {
					System.out.println(cursor.next().toJson());
				}
			}
		}
	}
}