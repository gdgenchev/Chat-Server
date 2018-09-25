package bg.chat.server;

import bg.chat.common.Message;
import bg.chat.common.MsgState;
import bg.chat.server.exceptions.UserAlreadyInChatRoomException;

import java.io.IOException;
import java.util.*;

public class ChatManager {
    private static ChatManager instance = null;
    private Map<String, User>  connectedUsers;
    private Map<String, ChatRoom> chatRooms;

    private ChatManager() {
        connectedUsers = new HashMap<>();
        chatRooms = new HashMap<>();
    }

    synchronized static ChatManager getInstance() {
        if (instance == null) {
           instance = new ChatManager();
        }
        return instance;
    }

    synchronized boolean login(String username, SocketHandlingThread socket) {
        User user = this.connectedUsers.get(username);
        if (user != null) {
            return false;
        }
        user = new User(username, socket);
        this.connectedUsers.put(username, user);
        return true;
    }

    synchronized void updateOnlineUsers() throws IOException {
        Set<String> usernames = new HashSet<>(connectedUsers.keySet());
        sendUsersList(usernames);
    }

    synchronized boolean sendMessageToUser(String from, String to, String message) throws IOException {
        User user = this.connectedUsers.get(to);
        if (user == null) {
            return false;
        }
        String[] data = {from, message};
        user.getSocket().writeMessage(new Message(MsgState.SEND_PRIVATE_SUCCESS, data));
        return true;
    }

    void disconnectUser(String username) {
        connectedUsers.remove(username);
    }

    private void sendUsersList(Set<String> usernames) throws IOException {
        for (User user : connectedUsers.values()) {
            usernames.remove(user.getUsername()); //O(1)
            String textToSend = String.join("\n", usernames);
            user.getSocket().writeMessage(new Message(MsgState.LIST_USERS, textToSend));
            usernames.add(user.getUsername()); //O(1)
        }
    }

    synchronized void createChatRoom(String creator, String chatRoomName) {
        User userCreator = connectedUsers.get(creator);
        ChatRoom chatRoom = new ChatRoom(userCreator, chatRoomName);
        chatRooms.put(chatRoomName,chatRoom);
    }

    synchronized void joinChatRoom(String user, String chatRoomName)
            throws UserAlreadyInChatRoomException {
        User joiner = connectedUsers.get(user);
        ChatRoom chatRoom = chatRooms.get(chatRoomName);
        chatRoom.addUser(joiner);
    }

    synchronized void sendMessageInChatRoom(String sender, String chatRoomName, String message) throws IOException {
        User senderUser = connectedUsers.get(sender);
        ChatRoom chatRoom = chatRooms.get(chatRoomName);
        chatRoom.broadcastMessage(sender, message);
    }

    public void updateOnlineChatRooms() throws IOException {
        Set<String> chatRoomNames = new HashSet<>(chatRooms.keySet());
        broadcastOnlineChatRoomNames(chatRoomNames);
    }

    private void broadcastOnlineChatRoomNames(Set<String> chatRoomNames) throws IOException {
        for (User user : connectedUsers.values()) {
            user.getSocket().writeMessage(
                    new Message(
                            MsgState.LIST_CHAT_ROOMS,
                            String.join("\n", chatRoomNames)));
        }
    }
}
