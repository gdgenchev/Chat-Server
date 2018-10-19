package bg.chat.server;

import bg.chat.common.Message;
import bg.chat.common.MessageType;
import bg.chat.server.exceptions.UserAlreadyInChatRoomException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static bg.chat.common.MessageType.JOIN_SUCCESS;

class ChatManager {
    private static ChatManager instance = null;
    private Map<String, User>  connectedUsers;
    private Map<String, ChatRoom> chatRooms;

    private FileService fileService;

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

    synchronized void updateOnlineUsers() {
        sendUsersList(new HashSet<>(connectedUsers.keySet()));
    }

    synchronized boolean sendMessageToUser(String from, String to, String message) {
        User receiver = this.connectedUsers.get(to);
        if (receiver == null) {
            return false;
        }
        String[] data = {from, message};
        receiver.getSocket().writeMessage(new Message(MessageType.SEND_PRIVATE_SUCCESS, data));
        return true;
    }

    synchronized void disconnectUser(String username) {
        connectedUsers.remove(username);
    }

    private void sendUsersList(Set<String> usernames) {
        for (User user : connectedUsers.values()) {
            usernames.remove(user.getUsername()); //O(1)
            String textToSend = String.join("\n", usernames);
            user.getSocket().writeMessage(new Message(MessageType.LIST_USERS, textToSend));
            usernames.add(user.getUsername()); //O(1)
        }
    }

    synchronized boolean createChatRoom(String creator, String chatRoomName) {
        User userCreator = connectedUsers.get(creator);
        ChatRoom chatRoom = new ChatRoom(userCreator, chatRoomName);
        if (chatRooms.containsKey(chatRoomName)) {
            return false;
        }
        chatRooms.put(chatRoomName,chatRoom);
        FileService.createFile(chatRoomName);
        return true;
    }

    synchronized boolean joinChatRoom(String user, String chatRoomName)
            throws UserAlreadyInChatRoomException {
        User joiner = connectedUsers.get(user);
        if (chatRooms.containsKey(chatRoomName)) {
            ChatRoom chatRoom = chatRooms.get(chatRoomName);
            chatRoom.addUser(joiner);
            String[] dataToSend = {
                    chatRoom.getCreator().getUsername(),
                    chatRoomName
            };
            joiner.getSocket().writeMessage(new Message(JOIN_SUCCESS, dataToSend));
            joiner.getSocket().writeMessage(new Message(
                    MessageType.SEND_GROUP,
                    chatRoom.getHistory()));
            return true;
        }

        return false;
    }

    synchronized void sendMessageInChatRoom(String sender, String chatRoomName, String message) {
        ChatRoom chatRoom = chatRooms.get(chatRoomName);
        chatRoom.broadcastMessage(sender, message);
    }

    synchronized void updateOnlineChatRooms() {
        Set<String> chatRoomNames = new HashSet<>(chatRooms.keySet());
        broadcastOnlineChatRoomNames(chatRoomNames);
    }

    private void broadcastOnlineChatRoomNames(Set<String> chatRoomNames) {
        for (User user : connectedUsers.values()) {
            user.getSocket().writeMessage(new Message(
                    MessageType.LIST_CHAT_ROOMS,
                    String.join("\n", chatRoomNames)));
        }
    }

    String getChatRoomOwner(String chatRoomName) {
        return chatRooms.get(chatRoomName).getCreator().getUsername();
    }

    synchronized void removeUserFromChatRoom(String username, String chatRoomName) {
        ChatRoom chatRoom = chatRooms.get(chatRoomName);
        chatRoom.removeUserFromChatRoom(username);
    }

    synchronized void deleteRoom(String roomName) {
        chatRooms.get(roomName).delete();
        chatRooms.remove(roomName);
    }

    synchronized void updateJoinedUsersForChatRoom(String chatRoomName) {
        chatRooms.get(chatRoomName).broadcastJoinedUsers();
    }
}
