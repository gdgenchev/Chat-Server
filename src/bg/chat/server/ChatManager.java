package bg.chat.server;

import java.io.IOException;
import java.util.*;

public class ChatManager {
    private static ChatManager instance = null;
    private Map<String, User>  connectedUsers;

    private ChatManager() {
        connectedUsers = new HashMap<>();
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
        user.getSocket().writeObject("SEND 1 " + from + " " + message);
        return true;
    }

    void disconnectUser(String username) {
        connectedUsers.remove(username);
    }

    private void sendUsersList(Set<String> usernames) throws IOException {
        for (User user : connectedUsers.values()) {
            usernames.remove(user.getUsername()); //O(1)
            String listUsersMessage = "LIST " + String.join("\n", usernames);
            System.out.println(listUsersMessage);
            System.out.println(user.getUsername());
            usernames.add(user.getUsername()); //O(1)
            user.getSocket().writeObject(listUsersMessage);

        }
    }
}
