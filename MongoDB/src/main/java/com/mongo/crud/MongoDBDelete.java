package com.mongo.crud;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoDBDelete {

	public static void main(String[] args) {

		/** Create connection with MongoClient. */
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {

			/** Open database. */
			MongoDatabase db = mongo.getDatabase("testdb");

			MongoCollection<Document> collection = db.getCollection("Persons");

			List<Document> docs = new ArrayList<Document>();
			docs.add(new Document("name", "Awadh"));
			docs.add(new Document("name", "Vinita").append("age", 25));
			collection.insertMany(docs);
			
			System.out.println("Before deletion of records. ");
			// Fetch all documents from the collection.
			try (MongoCursor<Document> cursor = collection.find().iterator()) {
				while (cursor.hasNext()) {
					System.out.println(cursor.next().toJson());
				}
			}
			
			// Delete all documents from collection.
			collection.deleteMany(Filters.or(Filters.eq("name", "Awadh"), Filters.eq("age", 25)));

			System.out.println("After deletion of records. No record found. ");
			// Fetch all documents from the collection.
			try (MongoCursor<Document> cursor = collection.find().iterator()) {
				while (cursor.hasNext()) {
					System.out.println(cursor.next().toJson());
				}
			}
		}
	}
}
