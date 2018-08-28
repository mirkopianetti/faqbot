package dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.trentinodigitale.faq.utils.ApplicationBuilder;
import org.bson.Document;

import java.util.logging.Level;

public abstract class MongoDB {

    public final ApplicationBuilder databaseBuilder = new ApplicationBuilder();

    MongoClient mongoClient;
    MongoCollection<Document> collection ;



    protected void connetti(){
        java.util.logging.Logger.getLogger(databaseBuilder.getMessageDB("database.driver")).setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI(databaseBuilder.getMessageDB("database.uri"));
        mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase(databaseBuilder.getMessageDB("database.name"));
        collection = database.getCollection(getCollection());
    }


    protected void disconnetti(){
        if (mongoClient!=null) {
            mongoClient.close();
        }
    }

    public Document getDocument(Long chat_id){
        connetti();
        if (chat_id==null)return null;
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("chat_id", chat_id);
        Document domanda =  collection.find(whereQuery).first();
        disconnetti();
        return domanda;
    }

    public Document getLastDocument(Long chat_id){
        connetti();
        if (chat_id==null)return null;

        BasicDBObject sortQuery = new BasicDBObject();
        sortQuery.put("data",-1);

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("chat_id", chat_id);

        Document domanda =  collection.find(whereQuery).sort(sortQuery).first();
        disconnetti();
        return domanda;
    }


    public abstract String getCollection();




}
