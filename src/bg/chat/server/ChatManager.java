package bg.chat.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatManager {
    private static ChatManager instance = null;

    private static Set<String> connectedUsernames;

    public synchronized static ChatManager getInstance() { //added synchronized after some research
        if (instance == null) {
            instance = new ChatManager();
            connectedUsernames = Collections.synchronizedSet(new HashSet<>());
        }
        return instance;
    }

    void login(String username) {
        connectedUsernames.add(username);
        System.out.println("User " + username + " added!");
    }


    public Collection<String> getAllUsers() {
        return connectedUsernames;
    }
}
