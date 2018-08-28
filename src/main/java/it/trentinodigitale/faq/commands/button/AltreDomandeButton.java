package it.trentinodigitale.faq.commands.button;

import dao.FaqSolr;
import dao.RicercheDB;
import it.trentinodigitale.faq.ContextBot;
import it.trentinodigitale.faq.commands.CommandsButton;
import org.apache.solr.common.SolrDocument;
import org.bson.Document;
import org.telegram.telegrambots.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;


public class AltreDomandeButton extends CommandsButton {


    public AltreDomandeButton(Update update) {
        this.update = update;
    }

    @Override
    public PartialBotApiMethod<Message> esegui(ContextBot context) {

        StringBuffer stringBuffer = new StringBuffer();

        String testoRicerca=
                context.message_text.substring(context.message_text.lastIndexOf(":") + 1);

        try {

            //Leggo dalla base dati l'ultima domanda effettuata

           RicercheDB ricercheDB = new RicercheDB();
            Document document = ricercheDB.getLastDocument(context.utilityBot.getChatID(update));
            System.out.println(document.get("domanda"));
            String domanda = document.get("domanda").toString();

            FaqSolr faqSolr = new FaqSolr();
            List<SolrDocument> listaRisultati = faqSolr.getResult(applicationBuilder.getMessageApplication("sorl.azienda"), null, null, domanda, 3);

            SendMessage sendMessage = new SendMessage();

            if (listaRisultati.size()>1){
                listaRisultati.stream().skip(1).map(solrDocument -> {
                    stringBuffer.append("\n---------------------\n" + solrDocument.getFieldValue("categoria") + " -  " + solrDocument.getFieldValue("sottocategoria").toString());
                    stringBuffer.append("\n<b>" + solrDocument.getFieldValue("domanda") + "</b>\n" + this.subString(solrDocument.getFieldValue("risposta").toString(),100 ) );

                    if(solrDocument.getFieldValue("url")!=null) {
                        stringBuffer.append(solrDocument.getFieldValue("url"));
                    }
                    stringBuffer.append("----------------------------------------------------\n");

                    return stringBuffer;
                }).count();



                sendMessage.setChatId(context.utilityBot.getChatID(update));

                stringBuffer.append("\n\n\n " +applicationBuilder.getMessageBundle("faq.altra.domanda"));
            }else {
                stringBuffer.append("\n\n\n "+applicationBuilder.getMessageBundle("faq.no.response"));

            }


            sendMessage.setText(stringBuffer.toString());

            sendMessage.enableHtml(true);
            return sendMessage;


        } catch (Exception e) {
            System.out.println(e.getStackTrace());

        }
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(context.utilityBot.getChatID(update));
        sendMessage.setText(stringBuffer.toString());
        sendMessage.enableHtml(true);
        return sendMessage;

    }


    private String subString (String valore, Integer numeroCaratteri){
        if (valore == null){
            return "";
        }

        if (valore.length()>numeroCaratteri){
            return valore.substring(0,numeroCaratteri);
        }else {
            return valore;
        }


    }

}
