package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.ChatRoomView;
import bg.chat.common.Message;
import bg.chat.common.MessageType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatRoomController {
    private Client client;
    private ChatRoomView chatRoomView;
    private static final Logger logger = LogManager.getLogger("Chat Room Controller");

    public ChatRoomController(Client client, ChatRoomView chatRoomView) {
        this.client       = client;
        this.chatRoomView = chatRoomView;
        this.chatRoomView.setCurrentUser(client.getUsername());
        this.chatRoomView.setCurrentChatRoom(client.getChatRoom());

        this.chatRoomView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String[] toSend = {client.getUsername(), chatRoomView.getChatRoomName()};
                client.writeMessage(new Message(MessageType.LEAVE_ROOM, toSend));
            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {
                chatRoomView.revalidate();
                chatRoomView.repaint();
            }
        });

        this.chatRoomView.addDeleteChatRoomListener(actionEvent ->
                client.writeMessage(new Message(
                        MessageType.DELETE_ROOM, chatRoomView.getChatRoomName())));

        this.chatRoomView.addSendListener(actionEvent -> {
            String[] toSend = {client.getUsername(), client.getChatRoom(),
                    chatRoomView.getSendMessageTextArea().getText()};
            chatRoomView.getReceivedMessagesTextArea().append(toSend[0] + ": " + toSend[2] + "\n");
            client.writeMessage(new Message(MessageType.SEND_GROUP, toSend));
        });
    }
}
