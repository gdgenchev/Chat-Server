package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.ChatRoomView;
import bg.chat.client.view.PrivateChatView;
import bg.chat.common.Message;
import bg.chat.common.MsgState;
import bg.chat.utils.DialogType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.*;
import java.io.IOException;

class PrivateChatController {
    private Client client;
    private PrivateChatView chatView;
    private static final Logger logger = LogManager.getLogger("Private Chat Controller");

    PrivateChatController(Client client, PrivateChatView chatView) {
        this.client = client;
        this.chatView = chatView;
        new UpdatePrivateChatViewThread(client, chatView).start();
        this.chatView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    client.writeMessage(new Message(MsgState.QUIT,client.getUsername()));
                } catch (IOException e1) {
                    logger.error("Exception thrown in Private Chat windowClosingListener", e);
                }
            }

        });
        this.chatView.addSendListener(new SendActionListener());
        this.chatView.setCurrentUsername(client.getUsername());
        this.chatView.addCreateChatRoomListener(new CreateChatRoomListener());
    }

    private class SendActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (client.getUsername().equals(chatView.getReceiver())) {
                chatView.showDialog("You can't send a message to yourself.", DialogType.ERROR);
                chatView.getSendMessageTextArea().setText("");
                return;
            }
            String message = chatView.getSendMessageTextArea().getText();
                try {
                    if (!message.isEmpty()) {
                        //Show sent message, only if the receiver is online
                        if (chatView.getOnlineUsersTextArea().getText().contains(chatView.getReceiver())) {
                            chatView.getReceivedMessagesTextArea()
                                    .append(client.getUsername() + ": " + message + "\n");
                        } else {
                            chatView.showDialog("User isn't online!", DialogType.ERROR);
                            return;
                        }
                        String[] data = {client.getUsername(), chatView.getReceiver(), message};
                        client.writeMessage(new Message(MsgState.SEND_PRIVATE, data));
                        chatView.getSendMessageTextArea().setText("");
                    }
                } catch (IOException e) {
                    logger.error("Exception thrown in Private Chat when Send Button clicked", e);
                }
        }
    }

    private class CreateChatRoomListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                client.setChatRoomName(chatView.getChatRoomName());
                String[] data = {client.getUsername(),chatView.getChatRoomName()};
                client.writeMessage(new Message(MsgState.CREATE, data));
                ChatRoomView chatRoomView = new ChatRoomView();
                new ChatRoomController(client, chatRoomView);
                chatRoomView.setCreator(client.getUsername());
                chatRoomView.setVisible(true);
            } catch (IOException e) {
                logger.error("Exception thrown in Private Chat when Create Button clicked", e);
            }
        }
    }
}
