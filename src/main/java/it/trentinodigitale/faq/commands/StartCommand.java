package it.trentinodigitale.faq.commands;

import com.vdurmont.emoji.EmojiParser;
import it.chatbase.generic.GenericMessage;
import it.chatbase.http.ChatBaseResponse;
import it.chatbase.model.Platform;
import it.chatbase.model.Type;
import it.trentinodigitale.faq.CommandsEnum;
import it.trentinodigitale.faq.ContextBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class StartCommand extends Commands{


    public StartCommand(Update update) {
        super();
        this.update = update;
    }

    @Override
    public SendMessage esegui(ContextBot context) {


        SendMessage message = new SendMessage().setChatId(context.utilityBot.getChatID(update));

        StringBuffer testoDiPresentazione = new StringBuffer("\n");
        testoDiPresentazione.append("Ciao  <b>" + update.getMessage().getChat().getFirstName() + "</b>\n");

        testoDiPresentazione.append(context.messageBundleBuilder.getMessage("faq.benvenuto"));

        GenericMessage genericMessage = genericMessageBuilder
                .withVersion("0.1")
                .withType(Type.USER.getType())
                .withUserId(message.getChatId())
                .withKey(applicationBuilder.getMessageApplication("chatBase.apiKey"))
                .withMessage("Ciao FAQBOT")
                .withNotHandled(false)
                .withIntent("start")
                .withPlatform(Platform.TELEGRAM.getPlatform()).build();


        ChatBaseResponse chatBaseResponse =  chatbaseClient.sendGenericMessage(genericMessage);
        System.out.println(" StartCommand - 1 " +  chatBaseResponse.toString());

         genericMessage = genericMessageBuilder
                .withVersion("0.1")
                .withType(Type.BOT.getType())
                .withUserId(message.getChatId())
                .withKey(applicationBuilder.getMessageApplication("chatBase.apiKey"))
                .withMessage(context.messageBundleBuilder.getMessage("faq.benvenuto"))
                .withNotHandled(false)
                .withIntent("start")
                .withPlatform(Platform.TELEGRAM.getPlatform()).build();

        chatBaseResponse =  chatbaseClient.sendGenericMessage(genericMessage);
        System.out.println(" StartCommand - 2 " + chatBaseResponse.toString());


//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        rowInline.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(" :sunrise_over_mountains: " + CommandsEnum.TRENTINO_SVILUPPO.toString())).setCallbackData(CommandsEnum.TRENTINO_SVILUPPO.getCommand()));
//        rowsInline.add(rowInline);
//
//        rowInline = new ArrayList<>();
//        rowInline.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(" :house: " + CommandsEnum.ALLOGGI.toString())).setCallbackData(CommandsEnum.ALLOGGI.getCommand()));
//        rowsInline.add(rowInline);
//
//        rowInline = new ArrayList<>();
//        rowInline.add(new InlineKeyboardButton().setText(EmojiParser.parseToUnicode(" :euro: " + CommandsEnum.IRAP.toString())).setCallbackData(CommandsEnum.IRAP.getCommand()));
//        rowsInline.add(rowInline);
//
//        // Add it to the message
//        markupInline.setKeyboard(rowsInline);
//        message.setReplyMarkup(markupInline);


        message.setText(testoDiPresentazione.toString()).enableHtml(true);
        return message;

    }
}
