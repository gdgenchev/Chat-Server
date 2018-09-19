package bg.chat.client.command;

public enum CommandType {

    CONNECT("CONNECT"), REGISTER("REGISTER"), LOGIN("LOGIN"), SEND("SEND"), DISCONNECT("DISCONNECT"), LIST_USERS("LIST-USERS");

    private String text;

    CommandType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static CommandType fromString(String text) {
        for (CommandType cmdType : CommandType.values()) {
            if (cmdType.text.equalsIgnoreCase(text)) {
                return cmdType;
            }
        }
        throw new IllegalArgumentException("No command with text " + text + " found");
    }
}
