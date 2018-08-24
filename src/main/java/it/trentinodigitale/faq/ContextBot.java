package it.trentinodigitale.faq;

import it.trentinodigitale.faq.utils.MessageBundleBuilder;
import it.trentinodigitale.faq.utils.UtilityBot;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ContextBot extends TelegramLongPollingBot {

     //La mappa ha per chiave il chat_id e per valore l'ultimo ticket inserito

     public final MessageBundleBuilder messageBundleBuilder = new MessageBundleBuilder();
     public final UtilityBot utilityBot = new UtilityBot();

     public String message_text;


}
