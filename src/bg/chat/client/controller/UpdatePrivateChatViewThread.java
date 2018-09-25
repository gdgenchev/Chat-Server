package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.PrivateChatView;
import bg.chat.common.Message;
import bg.chat.utils.DialogType;

import javax.swing.*;
import java.io.IOException;

class UpdatePrivateChatViewThread extends Thread {
    private Client client;
    private PrivateChatView chatView;

    UpdatePrivateChatViewThread(Client client, PrivateChatView chatView) {
        this.client = client;
        this.chatView = chatView;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message msg = client.readMessage();
                switch (msg.getState()) {
                    case LIST_USERS:
                        String onlineUsersToDisplay = (String) msg.getData();
                        SwingUtilities.invokeLater(() -> {
                            JTextArea onlineUsersTextArea = chatView.getOnlineUsersTextArea();
                            if (onlineUsersToDisplay.isEmpty()) {
                                onlineUsersTextArea.setText("No online users.");
                            } else if (!onlineUsersTextArea.getText().equals(onlineUsersToDisplay)) {
                                onlineUsersTextArea.setText(onlineUsersToDisplay);
                            }
                        });
                        break;
                    case SEND_PRIVATE_SUCCESS:
                        String[] data = (String[]) msg.getData();
                        String from = data[0];
                        String receivedText = data[1];
                        String textToDisplay = from + ": " + receivedText;
                        SwingUtilities.invokeLater(() ->
                                chatView.getReceivedMessagesTextArea()
                                        .append(textToDisplay + "\n"));
                        break;
                    //Double check if the receiver has left
                    case SEND_PRIVATE_FAIL:
                        chatView.showDialog("User isn't online at the moment!", DialogType.ERROR);
                        break;
                    case QUIT:
                        client.close();
                        return;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
