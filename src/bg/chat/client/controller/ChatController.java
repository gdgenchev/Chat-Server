package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.ChatView;
import bg.chat.utils.DialogType;

import java.awt.event.*;
import java.io.IOException;

class ChatController {
    private Client client;
    private ChatView chatView;

    ChatController(Client client, ChatView chatView) {
        this.client = client;
        this.chatView = chatView;
        new UpdateGUIThread(client, chatView).start();
        this.chatView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    client.writeLine("QUIT " + client.getUsername());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
       // this.chatView.addMouseListener(new OnClickMouseAdapter());
        this.chatView.addSendListener(new SendActionListener());
        this.chatView.getCurrentUser().setText(client.getUsername());
    }

    /**
     * Future idea: Click on online user from the online users text area
     * and get the history of your chat with him.
     * Sending message will not have receiver field -
     * it will be the clicked username.
     */
    /*private class OnClickMouseAdapter extends MouseAdapter implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                int line = chatView
                        .getOnlineUsersTextArea()
                        .getLineOfOffset(chatView
                                .getOnlineUsersTextArea()
                                .getCaretPosition());

                int start = chatView.getOnlineUsersTextArea().getLineStartOffset(line);

                int end = chatView.getOnlineUsersTextArea().getLineEndOffset(line);

                String user = chatView
                        .getOnlineUsersTextArea()
                        .getDocument()
                        .getText(start, end - start);

                System.out.println(user);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }
*/
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
                        chatView.getReceivedMessagesTextArea()
                                .append(client.getUsername() + ": " + message + "\n");
                        client.writeLine("SEND "
                                + client.getUsername() + " "
                                + chatView.getReceiver() + " "
                                + message);
                        chatView.getSendMessageTextArea().setText("");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
