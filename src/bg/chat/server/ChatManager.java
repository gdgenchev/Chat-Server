package bg.chat.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatManager {
    private static ChatManager instance = null;

    private static Set<String> connectedUsernames;

    public synchronized static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
            connectedUsernames = Collections.synchronizedSet(new HashSet<>());
        }
        return instance;
    }

    boolean login(String username) {
        if (isOnline(username)) {
            return false;
        } else {
            connectedUsernames.add(username);
            return true;
        }
    }

    public Collection<String> getAllUsers() {
        return connectedUsernames;
    }

    public boolean isOnline(String username) {
        return connectedUsernames.contains(username);
    }
}
