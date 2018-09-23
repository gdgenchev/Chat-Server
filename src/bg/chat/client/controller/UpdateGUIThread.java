package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.ChatView;
import bg.chat.utils.DialogType;

import javax.swing.*;
import java.io.IOException;

class UpdateGUIThread extends Thread {
    private Client client;
    private ChatView chatView;

    UpdateGUIThread(Client client, ChatView chatView) {
        this.client = client;
        this.chatView = chatView;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String textToDisplay;
                String line = (String) client.readObject();
                String[] lineData = line.split(" ");
                String cmd = lineData[0];
                if (cmd.equalsIgnoreCase("list")) {
                    String onlineUsersToDisplay = line.substring(5);
                    SwingUtilities.invokeLater(() -> {
                        JTextArea onlineUsersTextArea = chatView.getOnlineUsersTextArea();
                        if (onlineUsersToDisplay.isEmpty()) {
                            onlineUsersTextArea.setText("No online users.");
                        } else if (!onlineUsersTextArea.getText().equals(onlineUsersToDisplay)) {
                            onlineUsersTextArea.setText(onlineUsersToDisplay);
                        }
                    });
                } else if (cmd.equalsIgnoreCase("send")) {
                    int state = Integer.parseInt(lineData[1]);
                    if (state == 1) {
                        String from = lineData[2];
                        String receivedText = line.substring(cmd.length() + from.length() + 4);
                        textToDisplay = from + ": " + receivedText;
                        String finalTextToDisplay = textToDisplay;
                        SwingUtilities.invokeLater(() ->
                                chatView.getReceivedMessagesTextArea().append(finalTextToDisplay + "\n"));
                    } else if (state == 2) {
                        chatView.showDialog("User isn't online at the moment!", DialogType.ERROR);
                        chatView.getReceivedMessagesTextArea().append("Couldn't send message.\n");
                    }
                } else if (cmd.equalsIgnoreCase("quit")) {
                    client.close();
                    return;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
