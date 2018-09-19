package bg.chat.server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {
    private static ChatManager instance = null;

    private Map<String, User> connectedUsers;

    private ChatManager() {
        connectedUsers = new ConcurrentHashMap<>();
    }

    public synchronized static ChatManager getInstance() {
        if (instance == null) {
           instance = new ChatManager();
        }
        return instance;
    }

    boolean login(String username, SocketHandlingThread socket) {
        User user = this.connectedUsers.get(username);
        if (user != null) {
            return false;
        }
        user = new User(username, socket);
        this.connectedUsers.put(username, user);
        return true;
    }

    public Collection<String> getAllUsernames() {
        return this.connectedUsers.keySet();
    }


    public boolean sendMessageToUser(String from, String to, String message) throws IOException {
        User user = this.connectedUsers.get(to);
        if (user == null) {
            return false;
        }
        user.getSocket().writeLine("SEND 1 " + from + " " + message);
        return true;
    }
}
