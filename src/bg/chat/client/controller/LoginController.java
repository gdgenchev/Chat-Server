package bg.chat.client.controller;

import bg.chat.client.exceptions.FailedConnectionException;
import bg.chat.client.exceptions.WrongPasswordException;
import bg.chat.client.exceptions.WrongUsernameException;
import bg.chat.client.model.Client;
import bg.chat.client.view.LoginView;
import bg.chat.client.view.PrivateChatView;
import bg.chat.common.Message;
import bg.chat.common.MessageType;
import bg.chat.utils.Constants;
import bg.chat.utils.DialogType;
import bg.chat.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView loginView;
    private Client client;
    private static final Logger logger = LogManager.getLogger("Login Controller");

    public LoginController(Client client, LoginView loginView) {
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
                client.connectToServer(Constants.HOST, Constants.PORT);
                client.writeMessage(new Message(MessageType.LOGIN,client.getUsername()));

                if (client.readMessage().getType().equals(MessageType.LOGIN_SUCCESS)) {
                    PrivateChatView chatView = new PrivateChatView();
                    new PrivateChatController(client, chatView);
                    chatView.setVisible(true);
                    loginView.dispose();
                } else {
                    loginView.showDialog("You are already logged in", DialogType.ERROR);
                }
            } catch (WrongUsernameException | WrongPasswordException e) {
                loginView.showDialog(e.getMessage(), DialogType.ERROR);
            } catch (FailedConnectionException e) {
                loginView.showDialog("Error occurred while logging in", DialogType.ERROR);
                logger.warn("Exception thrown while logging in");
            }
        }
    }
}
