package bg.chat.client.view;

import bg.chat.client.view.components.SelfClosingDialog;
import bg.chat.utils.DialogType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class ChatRoomView extends JFrame {
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
    private JLabel chatRoomName;
    private JButton deleteChatRoomButton;
    private JLabel creator;
    private JTextField receiverTextField;

    public ChatRoomView() {
        setTitle("Chat Server");
        setContentPane(mainPanel);
        setLocationByPlatform(true);
        JScrollBar receiveMessagesScroll = receivedMessagesScrollPane.getVerticalScrollBar();
        JScrollBar sendMessagesScroll = sendMessageScrollPane.getVerticalScrollBar();
        sendMessagesScroll.setBackground(new Color(187, 83, 132));
        receiveMessagesScroll.setBackground(new Color(187, 83, 132));
        pack();
        getRootPane().setDefaultButton(sendButton);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        deleteChatRoomButton.setVisible(false);
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

    public void setCurrentUser(String username) {
        this.currentUser.setText(username);
    }

    public void setCurrentChatRoom(String chatRoomName) {
        this.chatRoomName.setText(chatRoomName);
    }

    public void showDeleteButton() {
        deleteChatRoomButton.setVisible(true);
    }

    public void setCreator(String creator) {
        this.creator.setText(creator);
    }

    public String getCreator() {
        return creator.getText();
    }

    public String getChatRoomName() {
        return chatRoomName.getText();
    }

    public void addDeleteChatRoomListener(ActionListener listenForDeleteButton) {
        this.deleteChatRoomButton.addActionListener(listenForDeleteButton);
    }
}
