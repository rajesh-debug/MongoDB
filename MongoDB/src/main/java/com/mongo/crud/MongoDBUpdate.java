package com.mongo.crud;

import static com.mongodb.client.model.Filters.eq;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * https://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
 * 
 * @author rajesh.dixit
 */
public class MongoDBUpdate {

	public static void main(String[] args) throws UnknownHostException {
		MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");

		// try with resource - auto closeable.
		try (MongoClient mongoClient = new MongoClient(connectionString)) {

			MongoDatabase db = mongoClient.getDatabase("testdb");

			MongoCollection<Document> collection = db.getCollection("Documents");

			Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1)
					.append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
					.append("info", new Document("x", 203).append("y", 102));

			collection.insertOne(doc);

			Document myDoc = collection.find(eq("name", "MongoDB")).first();
			System.out.println(myDoc.toJson());

			// Update single field in a single document
			collection.updateOne(eq("name", "MongoDB"),
					new Document("$set", new Document("versions", Arrays.asList("v4", "v3.2", "v3.0", "v2.6"))));

			myDoc = collection.find(eq("name", "MongoDB")).first();
			System.out.println(myDoc.toJson());
		}

	}

}
