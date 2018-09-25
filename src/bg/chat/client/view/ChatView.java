package bg.chat.client.view;

import bg.chat.client.view.components.SelfClosingDialog;
import bg.chat.utils.DialogType;

import javax.swing.*;
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
    private JLabel currentUser;
    private JTextField receiverTextField;
    private JTextArea onlineChatRoomsTextArea;
    private JTextField chatRoomName;
    private JButton joinButton;
    private JButton createButton;

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

    public String getReceiver() {
        return receiverTextField.getText();
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

    public JTextArea getSendMessageTextArea() {
        return sendMessageTextArea;
    }

    public void showDialog(String message, DialogType type) {
        new SelfClosingDialog(message, type.toString(), 1500).showDialogAndClose();
    }

    public void addCreateChatRoomListener(ActionListener listenForCreateButton) {
        createButton.addActionListener(listenForCreateButton);
    }

    public String getChatRoomName() {
        return chatRoomName.getText();
    }

    public void setCurrentUsername(String username) {
        this.currentUser.setText(username);
    }
}
