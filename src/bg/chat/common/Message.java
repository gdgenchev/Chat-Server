package bg.chat.common;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageType type;
    private Object data;

    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
