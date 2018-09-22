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
        new UpdateGUIThread(
                client,
                chatView.getReceivedMessagesTextArea(),
                chatView.getOnlineUsersTextArea())
                .start();
        new AskForOnlineUsersThread(client).start();
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
            String[] lines = chatView.getMessage().split("\n");
            for (String line : lines) {
                if (!chatView.getMessage().isEmpty()) {
                    try {
                        chatView.getReceivedMessagesTextArea()
                                .append(client.getUsername() + ": " + line + "\n");
                        client.writeLine("SEND "
                                + client.getUsername() + " "
                                + chatView.getReceiver() + " "
                                + line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    chatView.showDialog("Please type in a message", DialogType.ERROR);
                }
            }
        }
    }
}
