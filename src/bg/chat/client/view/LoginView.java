package bg.chat.client.view;

import bg.chat.utils.DialogType;
import bg.chat.client.view.components.SelfClosingDialog;
import bg.chat.utils.Constants;

import javax.swing.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JPanel         connectPanel;
    private JButton loginButton;
    private JTextField usernameTextField;
    private JPasswordField passwordField;

    public LoginView() {
        setTitle("Chat Server");
        setContentPane(connectPanel);
        pack();
        getRootPane().setDefaultButton(loginButton);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public void addLoginListener(ActionListener listenForLoginButton) {
        this.loginButton.addActionListener(listenForLoginButton);
    }

    public void showDialog(String message, DialogType type) {
        new SelfClosingDialog(
                 message, type.toString(), Constants.ERROR_MS)
                .showDialogAndClose();
    }
}