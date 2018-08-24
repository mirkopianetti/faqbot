package it.trentinodigitale.faq.utils;

import org.telegram.telegrambots.api.objects.Update;

public class UtilityBot {

    public Long getChatID(Update update){
        if (update.getMessage()!=null && update.getMessage().getChatId()!=null ){
            return update.getMessage().getChatId();
        }else if(update.getCallbackQuery()!=null && update.getCallbackQuery().getMessage()!=null && update.getCallbackQuery().getMessage().getChatId()!=null){
            return update.getCallbackQuery().getMessage().getChatId();
        }else {
            return  null;
        }
    }

}
