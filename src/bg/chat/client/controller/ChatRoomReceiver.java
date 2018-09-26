package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.ChatRoomView;
import bg.chat.common.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatRoomReceiver implements Receiver {
    private Client client;
    private ChatRoomView chatRoomView;
    private static final Logger logger = LogManager.getLogger("Chat Room Receiver");

    public ChatRoomReceiver(Client client, ChatRoomView chatRoomView) {
        this.client = client;
        this.chatRoomView = chatRoomView;
    }

    @Override
    public void respondToMessage(Message msg) {
        if (msg != null) {
            switch (msg.getType()) {
                case SEND_GROUP:
                    String[] data = (String[]) msg.getData();
                    chatRoomView
                            .getReceivedMessagesTextArea()
                            .append(data[0] + ": " + data[1] + "\n");
                    break;
                case LEAVE_ROOM:
                    chatRoomView.setVisible(false);
                    break;
                case JOINED_USERS:
                    String joinedUsers = (String)msg.getData();
                    if (joinedUsers.isEmpty()) {
                        chatRoomView.getOnlineUsersTextArea().setText("No online users");
                    } else {
                        chatRoomView.getOnlineUsersTextArea().setText(joinedUsers);
                    }
                    break;
            }
        }
    }
}
