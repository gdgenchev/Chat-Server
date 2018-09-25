package bg.chat.client;

import bg.chat.client.controller.LoginController;
import bg.chat.client.model.Client;
import bg.chat.client.view.LoginView;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            LoginView loginView = new LoginView();
            new LoginController(client, loginView);
            loginView.setVisible(true);
        });
    }
}
