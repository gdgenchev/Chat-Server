package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.exceptions.WrongPasswordException;
import bg.chat.client.exceptions.WrongUsernameException;
import bg.chat.client.view.ChatView;
import bg.chat.client.view.LoginView;
import bg.chat.utils.DialogType;
import bg.chat.utils.FileUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class LoginController {
    private LoginView loginView;
    private Client client;

    LoginController(Client client, LoginView loginView) {
        this.client = client;
        this.loginView = loginView;
        this.loginView.addLoginListener(new LoginListener());
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                String username = loginView.getUsername();
                char[] password = loginView.getPassword();
                FileUtils.checkUser(username, password);
                client.setUsername(username);
                client.writeLine("LOGIN " + client.getUsername());
                String line = (String) client.readObject();
                if (line.equalsIgnoreCase("LOGIN 1")) {
                    ChatView chatView = new ChatView();
                    new ChatController(client, chatView);
                    chatView.setVisible(true);
                    loginView.dispose();
                } else {
                    loginView.showDialog("You are already logged in", DialogType.ERROR);
                }
            } catch (WrongUsernameException | WrongPasswordException e) {
                loginView.showDialog(e.getMessage(), DialogType.ERROR);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
