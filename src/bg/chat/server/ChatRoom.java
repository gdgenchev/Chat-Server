package bg.chat.server;

import bg.chat.common.Message;
import bg.chat.common.MsgState;
import bg.chat.server.exceptions.UserAlreadyInChatRoomException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChatRoom {
    private String name;
    private User creator;
    private Set<User> joinedUsers;

    ChatRoom(User creator, String name) {
        this.creator = creator;
        this.name = name;
        this.joinedUsers = new HashSet<>();
        this.joinedUsers.add(creator);
    }

    void addUser(User user) throws UserAlreadyInChatRoomException {
        if (joinedUsers.contains(user)) {
            throw new UserAlreadyInChatRoomException();
        }
        joinedUsers.add(user);
    }

    void broadcastMessage(String sender, String message) throws IOException {
        for (User user : joinedUsers) {
            if (!user.getUsername().equals(sender)) {
                user.getSocket().writeMessage(new Message(MsgState.SEND_GROUP, message));
            }
        }
    }

    public void removeUserFromChatRoom(User user) {
        joinedUsers.remove(user);
    }
}
