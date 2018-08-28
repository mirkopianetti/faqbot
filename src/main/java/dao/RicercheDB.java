package dao;

import org.bson.Document;

import java.util.Date;

public class RicercheDB extends MongoDB{



    public void insertDocument(int chat_id, String domanda, Integer numeroRisultati){
        connetti();
//        long found = collection.count(Document.parse("{chat_id : " + Integer.toString(chat_id) + "}"));
//        if (found == 0) {
            Document doc = new Document("domanda", domanda)
                    .append("chat_id", chat_id)
                    .append("numFaq",numeroRisultati)
                    .append("data", new Date());

            collection.insertOne(doc);
            disconnetti();
//        }
    }

    @Override
    public String getCollection() {
        return "answers";
    }
}
