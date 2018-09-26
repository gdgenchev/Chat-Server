package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.PrivateChatView;
import bg.chat.common.Message;
import bg.chat.common.MessageType;
import bg.chat.utils.DialogType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class PrivateChatController {
    private Client client;
    private PrivateChatView chatView;
    private ReceiverBroadcaster receiverBroadcaster;

    PrivateChatController(Client client, PrivateChatView chatView) {
        this.client = client;
        this.chatView = chatView;

        this.receiverBroadcaster = new ReceiverBroadcaster(client);
        receiverBroadcaster.addReceiver(new PrivateChatReceiver(client, chatView, receiverBroadcaster));
        receiverBroadcaster.start();

        this.chatView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                client.writeMessage(new Message(MessageType.QUIT,client.getUsername()));
            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {
                chatView.revalidate();
                chatView.repaint();
            }
        });
        this.chatView.setCurrentUsername(client.getUsername());
        this.chatView.addSendListener(new SendActionListener());
        this.chatView.addCreateChatRoomListener(new CreateChatRoomListener());
        this.chatView.addJoinChatRoomListener(new JoinChatRoomListener());
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
                client.writeMessage(new Message(MessageType.SEND_PRIVATE, data));
                chatView.getSendMessageTextArea().setText("");
            }
        }
    }

    private class CreateChatRoomListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String[] data = {client.getUsername(),chatView.getChatRoomName()};
            client.writeMessage(new Message(MessageType.CREATE, data));
        }
    }

    private class JoinChatRoomListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String[] data = {client.getUsername(),chatView.getChatRoomName()};
            client.writeMessage(new Message(MessageType.JOIN, data));
        }
    }
}
