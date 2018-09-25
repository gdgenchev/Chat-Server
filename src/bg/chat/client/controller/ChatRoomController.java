package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.client.view.ChatRoomView;

public class ChatRoomController {
    private Client client;
    private ChatRoomView chatRoomView;

    public ChatRoomController(Client client, ChatRoomView chatRoomView) {
        this.client = client;
        this.chatRoomView = chatRoomView;
        this.chatRoomView.setCurrentUser(client.getUsername());
        this.chatRoomView.setCurrentChatRoom(client.getChatRoomName());
        //Show the delete button only to the creator
        if (chatRoomView.getCreator().equals(client.getUsername())) {
            chatRoomView.showDeleteButton();
        }
    }
}
