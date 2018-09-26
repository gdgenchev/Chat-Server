package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.ChatRoomView;
import bg.chat.client.view.PrivateChatView;
import bg.chat.common.Message;
import bg.chat.utils.DialogType;

import javax.swing.*;

class PrivateChatReceiver implements Receiver {
    private Client client;
    private PrivateChatView chatView;
    private ReceiverBroadcaster receiverBroadcaster;

    PrivateChatReceiver(Client client, PrivateChatView chatView, ReceiverBroadcaster receiverBroadcaster) {
        this.client = client;
        this.chatView = chatView;
        this.receiverBroadcaster = receiverBroadcaster;
    }

    @Override
    public void respondToMessage(Message msg) {
        if (msg != null) {
            switch (msg.getType()) {
                case LIST_USERS:
                    String onlineUsersToDisplay = (String) msg.getData();
                    SwingUtilities.invokeLater(() -> {
                        JTextArea onlineUsersTextArea = chatView.getOnlineUsersTextArea();
                        if (onlineUsersToDisplay.isEmpty()) {
                            onlineUsersTextArea.setText("No online users");
                        } else {
                            onlineUsersTextArea.setText(onlineUsersToDisplay);
                        }
                    });
                    break;
                case LIST_CHAT_ROOMS:
                    String onlineChatRoomNames = (String) msg.getData();
                    JTextArea onlineChatRooms = chatView.getOnlineChatRoomsTextArea();
                    if (onlineChatRoomNames.isEmpty()) {
                        onlineChatRooms.setText("No online rooms");
                    } else {
                        onlineChatRooms.setText(onlineChatRoomNames);
                    }
                    break;
                case SEND_PRIVATE_SUCCESS: {
                    String[] data = (String[]) msg.getData();
                    String from = data[0];
                    String receivedText = data[1];
                    String textToDisplay = from + ": " + receivedText;
                    chatView.getReceivedMessagesTextArea()
                            .append(textToDisplay + "\n");
                    chatView.repaint();
                    break;
                }
                //Double check if the receiver has left
                case SEND_PRIVATE_FAIL:
                    chatView.showDialog("User isn't online at the moment!", DialogType.ERROR);
                    break;
                case CREATE_SUCCESS:
                case JOIN_SUCCESS:
                    String[] data = (String[]) msg.getData();
                    goToChatRoom(data[0], data[1]);
                    break;
                case CREATE_FAIL:
                    chatView.showDialog("Chat room with that name already exists", DialogType.ERROR);
                    break;
                case JOIN_FAIL:
                    chatView.showDialog((String)msg.getData(), DialogType.ERROR);
                    break;
                case QUIT:
                    client.setConnected(false);
                    client.close();
            }
        }
    }

    private void goToChatRoom(String creator, String chatRoomName) {
        client.setChatRoom(chatRoomName);
        ChatRoomView chatRoomView = new ChatRoomView();
        chatRoomView.setCreator(creator);
        receiverBroadcaster.addReceiver(new ChatRoomReceiver(client, chatRoomView));

        new ChatRoomController(client, chatRoomView);
        if (client.getUsername().equals(creator)) {
            chatRoomView.showDeleteButton();
        }
        chatRoomView.setVisible(true);
    }
}
