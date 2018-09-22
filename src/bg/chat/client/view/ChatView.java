package bg.chat.client.view;

import bg.chat.client.view.CustomComponents.SelfClosingDialog;
import bg.chat.utils.DialogType;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class ChatView extends JFrame {
    private JPanel mainPanel;
    private JButton sendButton;
    private JPanel chatPanel;
    private JPanel messagePanel;
    private JScrollPane receivedMessagesScrollPane;
    private JScrollPane sendMessageScrollPane;
    private JTextArea sendMessageTextArea;
    private JTextArea receivedMessagesTextArea;
    private JTextArea onlineUsersTextArea;
    private JPanel onlineUsersPanel;
    private JScrollPane onlineUsersScrollPane;
    private JPanel sendPanel;
    private JTextField toTextField;
    private JLabel currentUser;

    public ChatView() {
        setTitle("Chat Server");
        setContentPane(mainPanel);
        setLocationByPlatform(true);
        JScrollBar receiveMessagesScroll = receivedMessagesScrollPane.getVerticalScrollBar();
        JScrollBar sendMessagesScroll = sendMessageScrollPane.getVerticalScrollBar();
        sendMessagesScroll.setBackground(new Color(187, 83, 132));
        receiveMessagesScroll.setBackground(new Color(187, 83, 132));
        pack();
        getRootPane().setDefaultButton(sendButton);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void addMouseListener(MouseAdapter listenForClickOnOnlineUsers) {
            onlineUsersTextArea.addMouseListener(listenForClickOnOnlineUsers);
    }

    public String getMessage() {
        return sendMessageTextArea.getText();
    }

    public String getReceiver() {
        return toTextField.getText();
    }

    public void addSendListener(ActionListener listenForSendButton) {
        this.sendButton.addActionListener(listenForSendButton);
    }

    public JTextArea getOnlineUsersTextArea() {
        return onlineUsersTextArea;
    }

    public JTextArea getReceivedMessagesTextArea() {
        return receivedMessagesTextArea;
    }

    public void showDialog(String message, DialogType type) {
        new SelfClosingDialog(message, type.toString(), 1500).showDialogAndClose();
    }

    public JLabel getCurrentUser() {
        return currentUser;
    }

}
