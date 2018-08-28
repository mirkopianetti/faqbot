package it.trentinodigitale.faq;

import it.trentinodigitale.faq.commands.Commands;
import it.trentinodigitale.faq.commands.DomandaCommand;
import it.trentinodigitale.faq.commands.StartCommand;
import it.trentinodigitale.faq.commands.TrentinoSviluppoCommand;
import it.trentinodigitale.faq.commands.button.AltreDomandeButton;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.Optional;

public class FaqBot extends ContextBot {


    public String getBotToken() {
        return messageBundleBuilder.getMessage("faq.botToken");
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            System.out.println("Il codice update ID è " + update.getUpdateId().toString());
            Long chat_id = update.getMessage().getChatId();
            System.out.println("Arrivata codice chat_id numero " + chat_id);
            operationCommand(update);

        } else if (update.hasCallbackQuery()) {
            operationButton(update);

        }
    }

    public String getBotUsername() {
        return "FaqTDBot";
    }

//    private void operationButton(Update update) {
//        String message_text = update.getCallbackQuery()==null?update.getMessageApplication().getText():update.getCallbackQuery().getData();
//        this.operation(update, message_text);
//    }


    private void operationButton(Update update) {
        message_text = update.getCallbackQuery()==null?update.getMessage().getText():update.getCallbackQuery().getData();



        //Controllo se il messaggio è presente nell'enum
        Optional<CommandsButtonEnum> isThereCommand = EnumSet.allOf(CommandsButtonEnum.class)
                .stream()
                .filter(commandsButtonEnum -> message_text.contains(commandsButtonEnum.getCommand())
                        || commandsButtonEnum.getLabel().contains(message_text))
                .findFirst();

        if (!isThereCommand.isPresent()) {
            this.operation(update, message_text);
        } else {

            if (isThereCommand.get().command.equals(CommandsButtonEnum.OTHER_ANSWER.command)) {
                AltreDomandeButton altreDomandeButton = new AltreDomandeButton(update);
                try {

                    execute((SendMessage) altreDomandeButton.esegui(this));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Comando arrivato " + message_text);
        }

    }




    private void operationCommand(Update update) {
        message_text = update.getMessage().getText();

        Optional<CommandsButtonEnum> isThereCommand = EnumSet.allOf(CommandsButtonEnum.class)
                .stream()
                .filter(commandsButtonEnum -> commandsButtonEnum.getLabel().equals(message_text))
                .findFirst();
        if (isThereCommand.isPresent()){
            this.operationButton(update);
        }else {
            this.operation(update, message_text);
        }

    }

    private void operation(Update update, String message_text) {

        Commands command;
        if (message_text.contains(CommandsEnum.START.command)) {

            SendPhoto sendPhotoRequest = new SendPhoto();
            sendPhotoRequest.setChatId(utilityBot.getChatID(update));


            InputStream stream = this.getClass().getResourceAsStream("/faq.jpg");
            sendPhotoRequest.setNewPhoto("Foto bot", stream);

            try {
                sendPhoto(sendPhotoRequest);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            command = new StartCommand(update);
            SendMessage sendMessage = command.esegui(this);
            /*getAuthButton(sendMessage);*/
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (message_text.contains(CommandsEnum.TRENTINO_SVILUPPO.command)) {
            command = new TrentinoSviluppoCommand(update);
            try {
                execute(command.esegui(this));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else{

            command = new DomandaCommand(update);
            try {
                execute(command.esegui(this));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }
}
