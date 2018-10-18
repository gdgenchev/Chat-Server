package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.PrivateChatView;
import bg.chat.common.Message;
import bg.chat.common.MessageType;
import bg.chat.utils.DialogType;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.*;

class PrivateChatController {
    private Client client;
    private PrivateChatView chatView;
    private ReceiverBroadcaster receiverBroadcaster;

    PrivateChatController(Client client, PrivateChatView chatView) {
        this.client = client;
        this.chatView = chatView;

        this.chatView.setCurrentUsername(client.getUsername());

        this.receiverBroadcaster = new ReceiverBroadcaster(client);

        receiverBroadcaster.addReceiver(new PrivateChatReceiver(client, chatView, receiverBroadcaster));
        receiverBroadcaster.start();

        //Lambda listeners
        this.chatView.addCreateChatRoomListener(listener-> {
            String[] data = {client.getUsername(),chatView.getChatRoomNameTextField()};
            client.writeMessage(new Message(MessageType.CREATE, data));
        });

        this.chatView.addJoinChatRoomListener(listener-> {
            String[] data = {client.getUsername(),chatView.getChatRoomNameTextField()};
            client.writeMessage(new Message(MessageType.JOIN, data));
        });

        //Private listeners
        this.chatView.addSendListener(new SendActionListener());
        this.chatView.addWindowListener(new CloseMinimizeWindowListener());
        this.chatView.addOnlineUsersListener(new OnClickOnlineUsersListener());
        this.chatView.addOnlineChatRoomsListener(new OnClickOnlineChatRoomsListener());
    }

    private String getTextOnClickFromTextArea(JTextArea textArea) {
        try {
            int line = textArea.getLineOfOffset(textArea.getCaretPosition() );
            int start = textArea.getLineStartOffset( line );
            int end = textArea.getLineEndOffset( line );
            return textArea.getDocument().getText(start, end - start);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Private Listeners
     *
     */
    private class CloseMinimizeWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            client.writeMessage(new Message(MessageType.QUIT,client.getUsername()));
        }

        @Override
        public void windowDeiconified(WindowEvent windowEvent) {
            chatView.revalidate();
            chatView.repaint();
        }
    }

    private class OnClickOnlineUsersListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JTextArea textArea = chatView.getOnlineUsersTextArea();
            String receiver = getTextOnClickFromTextArea(textArea);
            if (receiver != null && !receiver.equals("No online users")) {
                chatView.setReceiver(receiver);
            }
        }
    }

    private class OnClickOnlineChatRoomsListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JTextArea onlineChatRoomsTextArea = chatView.getOnlineChatRoomsTextArea();
            String chatRoom = getTextOnClickFromTextArea(onlineChatRoomsTextArea);
            if (chatRoom != null && !chatRoom.equals("No online rooms")) {
                chatView.setChatRoomTextField(chatRoom);
            }
        }
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
                if (!chatView.getOnlineUsersTextArea().getText().contains(chatView.getReceiver())) {
                    chatView.showDialog("User isn't online!", DialogType.ERROR);
                    return;
                }
                String[] data = {client.getUsername(), chatView.getReceiver(), message};
                client.writeMessage(new Message(MessageType.SEND_PRIVATE, data));
                chatView.getSendMessageTextArea().setText("");
            }
        }
    }
}
