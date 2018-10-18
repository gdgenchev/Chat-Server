package bg.chat.server;

import bg.chat.common.Message;
import bg.chat.common.MessageType;
import bg.chat.server.exceptions.UserAlreadyInChatRoomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ChatRoom {
    private String name;
    private User creator;
    private Map<String, User> joinedUsers;
    private static final Logger logger = LogManager.getLogger("Chat Room Manager");
    private String chatHistory;

    ChatRoom(User creator, String name) {
        this.creator = creator;
        this.name = name;
        this.joinedUsers = new HashMap<>();
        this.joinedUsers.put(creator.getUsername(), creator);
        System.out.println(creator.getUsername());
    }

    void addUser(User user) throws UserAlreadyInChatRoomException {
        if (joinedUsers.containsKey(user.getUsername())) {
            throw new UserAlreadyInChatRoomException();
        }
        joinedUsers.put(user.getUsername(), user);
    }

    void broadcastMessage(String message) {
        for (User user : joinedUsers.values()) {
            user.getSocket().writeMessage(new Message(MessageType.SEND_GROUP, message));
        }
    }

    void broadcastJoinedUsers() {
        sendListOfUsers(new HashSet<>(joinedUsers.keySet()));
    }

    private void sendListOfUsers(Set<String> usernames) {
        for (User user: joinedUsers.values()) {
            usernames.remove(user.getUsername()); //O(1)
            String textToSend = String.join("\n", usernames);
            user.getSocket().writeMessage(new Message(MessageType.JOINED_USERS, textToSend));
            usernames.add(user.getUsername()); //O(1)
        }
    }

    void removeUserFromChatRoom(String username) {
        joinedUsers.remove(username);
        logger.info(username + " removed from chat room " + name);
    }

    User getCreator() {
        return creator;
    }

    String getName() {
        return name;
    }

    void notifyUsersForDelete() {
        for (User user : joinedUsers.values()) {
            user.getSocket().writeMessage(new Message(MessageType.LEAVE_ROOM));
        }
    }
}
