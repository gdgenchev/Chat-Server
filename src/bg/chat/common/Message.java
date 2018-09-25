package bg.chat.common;

import java.io.Serializable;

public class Message implements Serializable {
    private MsgState state;
    private Object data;

    public Message(MsgState state) {
        this.state = state;
    }

    public Message(MsgState state, Object data) {
        this.state = state;
        this.data = data;
    }

    public MsgState getState() {
        return state;
    }

    public Object getData() {
        return data;
    }
}
