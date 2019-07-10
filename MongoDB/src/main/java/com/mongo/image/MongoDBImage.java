package com.mongo.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

/**
 * https://kb.objectrocket.com/mongo-db/how-to-save-an-image-in-mongodb-using-java-394
 * 
 * @author rajesh.dixit
 */
public class MongoDBImage {

	public static void main(String[] args) throws FileNotFoundException {
		/** Create connection with MongoClient. */
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {

			String filePath = "/Users/rajesh.dixit/Desktop/code_drive/mongodb/src/main/photo200.jpeg";
			String fileName = "photo200.jpeg";
			/** Open database. */
			MongoDatabase imgDb = mongo.getDatabase("imageDatabase");

			// Create a gridFSBucket
			GridFSBucket gridBucket = GridFSBuckets.create(imgDb);

			InputStream inStream = new FileInputStream(new File(filePath));

			// Create some customize options for the details that describes the uploaded
			// image
			GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024)
					.metadata(new Document("type", "image").append("content_type", "image/png"));

			ObjectId fileId = gridBucket.uploadFromStream(fileName, inStream, uploadOptions);
			System.out.println(fileId);

			OutputStream outputStream = new FileOutputStream(new File("/Users/rajesh.dixit/Desktop/code_drive/mongodb/src/main/"+fileId+".jpeg"));
			gridBucket.downloadToStream(fileId, outputStream);
		}
	}
}
