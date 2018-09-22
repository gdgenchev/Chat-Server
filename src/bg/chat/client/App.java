package bg.chat.client;

import bg.chat.client.controller.ConnectController;
import bg.chat.client.model.Client;
import bg.chat.client.view.ConnectView;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            ConnectView connectView = new ConnectView();
            new ConnectController(client, connectView);
            connectView.setVisible(true);
        });
    }
}
