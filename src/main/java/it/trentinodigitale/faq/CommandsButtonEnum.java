package it.trentinodigitale.faq;

public enum CommandsButtonEnum {

    OTHER_ANSWER("OTHER:param", "OTHER");


    String command;
    String label;


    CommandsButtonEnum(String command, String label) {

        this.command = command;
        this.label = label;

    }

    public String getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }
    }
