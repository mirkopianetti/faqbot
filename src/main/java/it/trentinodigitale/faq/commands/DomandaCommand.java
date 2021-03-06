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


            if (listaRisultati.size()==0){
                testoDiPresentazione.append("NON ho trovato risposte inerenti la dua domanda, puoi riformularla in modo differente?: \n");

                 chatbaseClient = new ChatbaseClient();

                 ChatBaseResponse chatBaseResponse =  chatbaseClient.sendGenericMessage(
                        genericMessageBuilder
                                .withMessage(update.getMessage().getText())
                                .withVersion("0.1")
                                .withType(Type.USER.getType())
                                .withUserId(message.getChatId())
                                .withKey(applicationBuilder.getMessageApplication("chatBase.apiKey"))
                                .withNotHandled(true)
                                .withPlatform(Platform.TELEGRAM.getPlatform())
                                .withIntent("start").build());
                System.out.println(" DomandaCommand - 2 " +  chatBaseResponse.toString());

            }else {
                if (listaRisultati.size()>0) {

                    SolrDocument solrDocument = listaRisultati
                            .stream()
                            .findFirst().get();

                    scriviPrimaFaq(solrDocument, testoDiPresentazione, message);

                    ChatBaseResponse chatBaseResponse =  chatbaseClient.sendGenericMessage(
                            genericMessageBuilder
                                    .withMessage(solrDocument.getFieldValue("domanda").toString())
                                    .withVersion("0.1")
                                    .withType(Type.BOT.getType())
                                    .withUserId(message.getChatId())
                                    .withKey(applicationBuilder.getMessageApplication("chatBase.apiKey"))
                                    .withNotHandled(false)
                                    .withPlatform(Platform.TELEGRAM.getPlatform())
                                    .withIntent("start").build());
                    System.out.println(" DomandaCommand - 2 " +  chatBaseResponse.toString());



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
