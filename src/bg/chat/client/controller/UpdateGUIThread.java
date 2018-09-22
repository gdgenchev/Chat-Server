package bg.chat.client.controller;

import bg.chat.client.model.Client;

import javax.swing.*;
import java.io.IOException;

class UpdateGUIThread extends Thread {
    private Client client;
    private JTextArea receivedMessagesTextArea;
    private JTextArea onlineUsersTextArea;

    UpdateGUIThread(Client client,
                    JTextArea receivedMessagesTextArea,
                    JTextArea onlineUsersTextArea) {
        this.client = client;
        this.receivedMessagesTextArea = receivedMessagesTextArea;
        this.onlineUsersTextArea = onlineUsersTextArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                StringBuilder onlineUsers = new StringBuilder();
                String textToDisplay = null;
                String line = client.readLine();
                String[] lineData = line.split(" ");
                String cmd = lineData[0];
                if (line.equalsIgnoreCase("list-users 0")) {
                    boolean has = false;
                    while (!line.equalsIgnoreCase("list-users 1")) {
                        line = client.readLine();
                        if (!line.equalsIgnoreCase("list-users 1")) {
                            if (!line.equalsIgnoreCase(client.getUsername())) {
                                has = true;
                                onlineUsers.append(line).append("\n");
                            }
                        }
                    }
                    if (!has) {
                        onlineUsers.append("No online users");
                    }
                    SwingUtilities.invokeLater(() ->
                    {
                        if (!onlineUsersTextArea.getText().equals(onlineUsers.toString())) {
                            onlineUsersTextArea.setText(onlineUsers.toString());
                        }
                    });
                } else if (cmd.equalsIgnoreCase("send")) {
                    int state = Integer.parseInt(lineData[1]);
                    if (state == 1) {
                        String from = lineData[2];
                        String receivedText = line.substring(cmd.length() + from.length() + 4);
                        textToDisplay = from + ": " + receivedText;

                    } else if (state == 2) {
                        textToDisplay = "The user is not online";
                    }
                    String finalTextToDisplay = textToDisplay;
                    SwingUtilities.invokeLater(() ->
                            receivedMessagesTextArea.append(finalTextToDisplay + "\n"));
                } else if (cmd.equalsIgnoreCase("quit")) {
                    client.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
