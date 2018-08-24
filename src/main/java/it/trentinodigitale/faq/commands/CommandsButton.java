package it.trentinodigitale.faq.commands;

import it.trentinodigitale.faq.ContextBot;
import org.telegram.telegrambots.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

public abstract class CommandsButton {
    protected Update update;

    public abstract PartialBotApiMethod<Message> esegui(ContextBot context);


 /*   protected Long getChatID(){
        if (update.getMessage()!=null && update.getMessage().getChatId()!=null ){
            return update.getMessage().getChatId();
        }else if(update.getCallbackQuery()!=null && update.getCallbackQuery().getMessage()!=null && update.getCallbackQuery().getMessage().getChatId()!=null){
            return update.getCallbackQuery().getMessage().getChatId();
        }else {
            return  null;
        }


    }*/
}
