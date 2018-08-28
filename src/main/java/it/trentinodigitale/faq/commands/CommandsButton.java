package it.trentinodigitale.faq.commands;

import it.trentinodigitale.faq.ContextBot;
import it.trentinodigitale.faq.utils.ApplicationBuilder;
import org.telegram.telegrambots.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

public abstract class CommandsButton {
    protected Update update;

    public abstract PartialBotApiMethod<Message> esegui(ContextBot context);

    protected ApplicationBuilder applicationBuilder = new ApplicationBuilder();

 /*   protected Long getChatID(){
        if (update.getMessageApplication()!=null && update.getMessageApplication().getChatId()!=null ){
            return update.getMessageApplication().getChatId();
        }else if(update.getCallbackQuery()!=null && update.getCallbackQuery().getMessageApplication()!=null && update.getCallbackQuery().getMessageApplication().getChatId()!=null){
            return update.getCallbackQuery().getMessageApplication().getChatId();
        }else {
            return  null;
        }


    }*/
}
