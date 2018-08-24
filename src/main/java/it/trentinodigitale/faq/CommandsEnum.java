package it.trentinodigitale.faq;

public enum CommandsEnum {

    START("/start"),
    TRENTINO_SVILUPPO("/trentino-sviluppo"),
    ALLOGGI("/alloggi"),
    IRAP("/irap"),
    HELP("/help");

    String command;

    CommandsEnum(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
