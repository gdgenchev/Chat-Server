package bg.chat.client.view;

import bg.chat.utils.DialogType;
import bg.chat.client.view.CustomComponents.SelfClosingDialog;
import bg.chat.utils.Constants;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ConnectView extends JFrame {
    private JPanel     connectPanel;
    private JButton    connectButton;
    private JTextField hostTextField;
    private JTextField portTextField;

    public ConnectView() {
        setTitle("Chat Server");
        setContentPane(connectPanel);
        pack();
        getRootPane().setDefaultButton(connectButton);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public String getHost() {
        return hostTextField.getText();
    }

    public String getPortAsString() {
        return portTextField.getText();
    }

    public void addConnectListener(ActionListener listenForConnectButton) {
        this.connectButton.addActionListener(listenForConnectButton);
    }

    public void showDialog(String message, DialogType type) {
        new SelfClosingDialog(message, type.toString(), Constants.ERROR_MS).showDialogAndClose();
    }
}
