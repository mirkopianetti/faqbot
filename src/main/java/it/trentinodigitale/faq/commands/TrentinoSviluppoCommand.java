package it.trentinodigitale.faq.commands;

import it.trentinodigitale.faq.ContextBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class TrentinoSviluppoCommand extends Commands {

    public TrentinoSviluppoCommand(Update update) {
        super();
        this.update = update;

    }


    @Override
    public SendMessage esegui(ContextBot context) {
        SendMessage message = new SendMessage().setChatId(context.utilityBot.getChatID(update));

        StringBuffer testoDiPresentazione = new StringBuffer("\n");
        testoDiPresentazione.append("Benvenuto nell'area di TRENTINO SVILUPPO, \nCome posso esserti utile? Scrivi la tua domanda");
        message.setText(testoDiPresentazione.toString()).enableHtml(true);

        return message;
    }
}
