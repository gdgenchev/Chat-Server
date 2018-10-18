package bg.chat.client.view;

import bg.chat.client.view.components.SelfClosingDialog;
import bg.chat.utils.DialogType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class PrivateChatView extends JFrame {
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
    private JTextField chatRoomNameTextField;
    private JButton joinButton;
    private JButton createButton;

    public PrivateChatView() {
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

    //Setters
    public void setCurrentUsername(String username) {
        this.currentUser.setText(username);
    }

    public void setReceiver(String receiver) {
        this.receiverTextField.setText(receiver);
    }

    public void setChatRoomTextField(String chatRoom) {
        this.chatRoomNameTextField.setText(chatRoom);
    }

    //Getters
    public JTextArea getOnlineUsersTextArea() {
        return onlineUsersTextArea;
    }

    public JTextArea getReceivedMessagesTextArea() {
        return receivedMessagesTextArea;
    }

    public JTextArea getSendMessageTextArea() {
        return sendMessageTextArea;
    }

    public JTextArea getOnlineChatRoomsTextArea() {
        return onlineChatRoomsTextArea;
    }

    public String getChatRoomNameTextField() {
        return chatRoomNameTextField.getText();
    }

    public String getReceiver() {
        return receiverTextField.getText();
    }

    //Dialog
    public void showDialog(String message, DialogType type) {
        new SelfClosingDialog(message, type.toString(), 1500).showDialogAndClose();
    }

    //Add Listeners
    public void addOnlineUsersListener(MouseListener listenForUserClick) {
        this.onlineUsersTextArea.addMouseListener(listenForUserClick);
    }

    public void addOnlineChatRoomsListener(MouseListener listenForChatRoomClick) {
        this.onlineChatRoomsTextArea.addMouseListener(listenForChatRoomClick);
    }

    public void addSendListener(ActionListener listenForSendButton) {
        this.sendButton.addActionListener(listenForSendButton);
    }

    public void addJoinChatRoomListener(ActionListener listenForJoinButton) {
        joinButton.addActionListener(listenForJoinButton);
    }

    public void addCreateChatRoomListener(ActionListener listenForCreateButton) {
        createButton.addActionListener(listenForCreateButton);
    }
}
