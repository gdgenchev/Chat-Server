package bg.chat.server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {
    private static ChatManager instance = null;

    private static Map<String, User> connectedUsers;

    private ChatManager() {
        instance = new ChatManager();
        connectedUsers = new ConcurrentHashMap<>();
    }

    public synchronized static ChatManager getInstance() {
        if (instance == null) {
           instance = new ChatManager();
        }
        return instance;
    }

    boolean login(String username, SocketHandlingThread socket) {
        if (isOnline(username)) {
            return false;
        }
        connectedUsers.put(username, new User(username, socket));
        return true;
    }

    public Collection<String> getAllUsernames() {
        return connectedUsers.keySet();
    }

    public boolean isOnline(String username) {
        return connectedUsers.get(username) != null;
    }
}
