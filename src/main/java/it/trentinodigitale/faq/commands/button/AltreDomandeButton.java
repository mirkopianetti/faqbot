package it.trentinodigitale.faq.commands.button;

import dao.FaqSolr;
import it.trentinodigitale.faq.ContextBot;
import it.trentinodigitale.faq.commands.CommandsButton;
import org.apache.solr.common.SolrDocument;
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
            FaqSolr faqSolr = new FaqSolr();
            List<SolrDocument> listaRisultati = faqSolr.getResult("3", null, null, testoRicerca, 3);

            SendMessage sendMessage = new SendMessage();

            if (listaRisultati.size()>1){
                listaRisultati.stream().skip(1).map(solrDocument -> {
                    stringBuffer.append("\n---------------------\n" + solrDocument.getFieldValue("categoria") + " -  " + solrDocument.getFieldValue("sottocategoria").toString());
                    stringBuffer.append("\n<b>" + solrDocument.getFieldValue("domanda") + "</b>\n" + solrDocument.getFieldValue("risposta").toString());
                    return stringBuffer;
                }).count();


                sendMessage.setChatId(context.utilityBot.getChatID(update));
                stringBuffer.append("\n\n\n Possiamo aiutarti ancora? Scrivi un'altra domanda");
            }else {
                stringBuffer.append("\n\n\n Non sono presenti altre FAQ, prova a specificare meglio il problema");

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

}
