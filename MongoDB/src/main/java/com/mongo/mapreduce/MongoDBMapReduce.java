package com.mongo.mapreduce;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoDBMapReduce {

	public static void main(String[] args) throws UnknownHostException {

		/** Create connection with MongoClient. */
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {

			/** Open database. */
			MongoDatabase db = mongo.getDatabase("testdb");

			MongoCollection<Document> collection = db.getCollection("Persons");

			// Delete all documents from collection.
			collection.deleteMany(Filters.or(Filters.eq("name", "Awadh"), Filters.eq("name", "Vinita"), Filters.eq("name", "Rajesh")));

			List<Document> persons = new ArrayList<Document>();
			persons.add(new Document("name", "Awadh").append("age", 61));
			persons.add(new Document("name", "Vinita").append("age", 25));
			persons.add(new Document("name", "Rajesh").append("age", 30));

			// insert multiple records in mongo db.
			collection.insertMany(persons);

			// Fetch all documents from the collection.
			try (MongoCursor<Document> cursor = collection.find().iterator()) {
				while (cursor.hasNext()) {
					System.out.println(cursor.next().toJson());
				}
			}

			// map function to categorize young guys only
			String youngMap = "function () {" + "var criteria;" + "if ( this.age <= 30 ) {" + "criteria = 'young';"
					+ "emit(criteria, this.age);" + "}" + "};";

			// reduce function to add all the age and calculate the average age
			String youngReduce = "function(key, age) {" + "var total = 0;" + "for (var i = 0; i < age.length; i++) {"
					+ "total = total + age[i];" + "}" + "return total/age.length;" + "};";

			MongoCursor<Document> youngMapReduce = collection.mapReduce(youngMap, youngReduce).iterator();
			while (youngMapReduce.hasNext()) {
				Document resDoc = youngMapReduce.next();
				System.out.println(resDoc);
			}
			
			// map function to categorize senior guys only
			String seniorMap = "function () {" + "var criteria;" + "if ( this.age > 60 ) {"
					+ "criteria = 'Senior citizen';" + "emit(criteria, this.age);" + "}" + "};";

			// reduce function to add all the age and calculate the average age
			String seniorReduce = "function(key, age) {" + "var total = 0;" + "for (var i = 0; i < age.length; i++) {"
					+ "total = total + age[i];" + "}" + "return total/age.length;" + "};";
			
			MongoCursor<Document> seniorMapReduce = collection.mapReduce(seniorMap, seniorReduce).iterator();
			while (seniorMapReduce.hasNext()) {
				Document resDoc = seniorMapReduce.next();
				System.out.println(resDoc);
			}
		}
	}
}
