package it.trentinodigitale.faq.commands;

import com.vdurmont.emoji.EmojiParser;
import dao.FaqSolr;
import dao.RicercheDB;
import it.chatbase.client.ChatbaseClient;
import it.chatbase.generic.GenericMessage;
import it.chatbase.generic.GenericMessageBuilder;
import it.chatbase.http.ChatBaseResponse;
import it.chatbase.model.Platform;
import it.chatbase.model.Type;
import it.trentinodigitale.faq.ContextBot;
import org.apache.solr.common.SolrDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class DomandaCommand extends Commands{

    GenericMessageBuilder genericMessageBuilder = new GenericMessageBuilder();

    public DomandaCommand(Update update) {
        super();
        this.update = update;
    }

    @Override
    public SendMessage esegui(ContextBot context) {


        SendMessage message = new SendMessage().setChatId(context.utilityBot.getChatID(update));

        StringBuffer testoDiPresentazione = new StringBuffer("\n");

        try {
            FaqSolr faqSolr = new FaqSolr();
            List<SolrDocument> listaRisultati = faqSolr.getResult(applicationBuilder.getMessageApplication("sorl.azienda"),null,null, update.getMessage().getText(),null);

            //Salvo la domanda nella base dati
            RicercheDB ricercheDB  = new RicercheDB();
            ricercheDB.insertDocument(new Integer(message.getChatId()),update.getMessage().getText(),listaRisultati.size());


            //Chiamo il client Chatbase per le statistiche

/*            String jsonString = new JSONObject()
                    .put("api_key", applicationBuilder.getMessageApplication("chatBase.apiKey"))
                    .put("type",applicationBuilder.getMessageApplication("chatBase.type"))
                    .put("platform", "Telegram")
                    .put("intent", "domanda")
                    .put("message", update.getMessage().getText())
                    .put("user_id", message.getChatId()).toString();

            clientHttpJson.sendPost(applicationBuilder.getMessageApplication("chatBase.url"),jsonString);*/




            GenericMessage genericMessage = genericMessageBuilder
                    .withVersion("0.1")
                    .withType(Type.BOT.getType())
                    .withUserId(message.getChatId())
                    .withKey(applicationBuilder.getMessageApplication("chatBase.apiKey"))
                    .withMessage(update.getMessage().getText())
                    .withNotHandled(false)
                    .withIntent("domanda")
                    .withPlatform(Platform.TELEGRAM.getPlatform()).build();



            ChatbaseClient chatbaseClient = new ChatbaseClient();
            ChatBaseResponse chatBaseResponse =  chatbaseClient.sendGenericMessage(genericMessage);

            System.out.println(chatBaseResponse.getReason());



            if (listaRisultati.size()==0){
                testoDiPresentazione.append("NON ho trovato risposte inerenti la dua domanda, puoi riformularla in modo differente?: \n");
            }else {
                if (listaRisultati.size()>0) {

                    SolrDocument solrDocument = listaRisultati
                            .stream()
                            .findFirst().get();

                    scriviPrimaFaq(solrDocument, testoDiPresentazione, message);
                }
                if (listaRisultati.size()>1) {
                    preparaBottoni(message);
                }
                preparaBottoniInLinea(message,context);

            }

        }catch (Exception ie){

        }


        message.setText(testoDiPresentazione.toString()).enableHtml(true);

        return message;

    }


    private void scriviPrimaFaq(SolrDocument solrDocument, StringBuffer stringBuffer, SendMessage message) throws Exception{

        stringBuffer.append("<b>" + solrDocument.getFieldValue("domanda")  + "</b> \n" );

        String risposta = solrDocument.getFieldValue("risposta").toString();


        ChatbaseClient chatbaseClient = new ChatbaseClient();
        ChatBaseResponse chatBaseResponse =  chatbaseClient.sendGenericMessage(genericMessageBuilder.withMessage(risposta).build());
        System.out.println(chatBaseResponse.getReason());

        stringBuffer.append(risposta.length()>200?risposta.substring(0,200):risposta + "\n");

        if(solrDocument.getFieldValue("url")!=null) {
            stringBuffer.append(solrDocument.getFieldValue("url"));
        }
        stringBuffer.append("----------------------------------------------------\n");

    }

    private void preparaBottoni(SendMessage message){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        // first keyboard line
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardAppartamento = new KeyboardButton();
        keyboardAppartamento.setText("PRIMO");
        keyboardFirstRow.add(keyboardAppartamento);

        KeyboardButton keyboardPartiComuni = new KeyboardButton();
        keyboardPartiComuni.setText("SECONDO");
        keyboardFirstRow.add(keyboardPartiComuni);

        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(replyKeyboardMarkup);

    }

    private void preparaBottoniInLinea(SendMessage message, ContextBot context){

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

//        context.message_text


        rowInline.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(" :mega: ALTRE RISPOSTE" )).setCallbackData("OTHER:param:" ));
        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(" :woman: Risposta trovata - GRAZIE" )).setCallbackData("THANK"));
        rowsInline.add(rowInline);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);


    }


    private void scriviAltreFaq(SolrDocument solrDocument, StringBuffer stringBuffer){

        stringBuffer.append(solrDocument.getFieldValue("id") + " " + solrDocument.getFieldValue("domanda") + "\n");


    }

}
