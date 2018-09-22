package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.exceptions.FailedConnectionException;
import bg.chat.client.view.ConnectView;
import bg.chat.client.view.LoginView;
import bg.chat.utils.DialogType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectController {
    private Client      client;
    private ConnectView connectView;

    public ConnectController(Client client, ConnectView connectView) {
        this.client = client;
        this.connectView = connectView;
        this.connectView.addConnectListener(new ConnectListener());
    }


    private class ConnectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                client.connectToServer(connectView.getHost(),
                             Integer.parseInt(connectView.getPortAsString()));
                LoginView loginView = new LoginView();
                new LoginController(client, loginView);
                loginView.setVisible(true);
                connectView.dispose();
            } catch (NumberFormatException e) {
                connectView.showDialog("Port should be a number!", DialogType.ERROR);
            } catch (FailedConnectionException e) {
                connectView.showDialog(e.getMessage(), DialogType.ERROR);
            }
        }
    }
}
