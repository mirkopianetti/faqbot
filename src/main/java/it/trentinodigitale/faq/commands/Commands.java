package it.trentinodigitale.faq.commands;

import it.chatbase.client.ChatbaseClient;
import it.chatbase.generic.GenericMessageBuilder;
import it.trentinodigitale.faq.ContextBot;
import it.trentinodigitale.faq.utils.ApplicationBuilder;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Commands {



    ApplicationBuilder applicationBuilder = new ApplicationBuilder();
    ChatbaseClient chatbaseClient = new ChatbaseClient();
    GenericMessageBuilder genericMessageBuilder = new GenericMessageBuilder();

    protected Update update;

    public abstract SendMessage esegui(ContextBot context);


    protected void log(String first_name, String last_name, String user_id, String txt, String bot_answer, String numeroTelefono) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + " + telefono: " + numeroTelefono +") \n Text - " + txt);

        System.out.println("Bot answer: \n Text - " + bot_answer);
    }

   /* protected Long getChatID(){
        if (update.getMessageApplication()!=null && update.getMessageApplication().getChatId()!=null ){
            return update.getMessageApplication().getChatId();
        }else if(update.getCallbackQuery()!=null && update.getCallbackQuery().getMessageApplication()!=null && update.getCallbackQuery().getMessageApplication().getChatId()!=null){
            return update.getCallbackQuery().getMessageApplication().getChatId();
        }else {
            return  null;
        }


    }*/
}
