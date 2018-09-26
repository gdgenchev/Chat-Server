package bg.chat.server;

import bg.chat.common.Message;
import bg.chat.server.exceptions.NonExistentChatRoomException;
import bg.chat.server.exceptions.UserAlreadyInChatRoomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import static bg.chat.common.MessageType.*;

public class SocketHandlingThread extends Thread {
    private final Socket socket;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;
    private String loggedInUser;
    private static final Logger logger = LogManager.getLogger("Socket Handling Thread Logger");

    SocketHandlingThread(Socket socket) {
        this.socket = socket;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.warn("Exception thrown while writing constructing the SocketHandlingThread");
        }
    }

    synchronized void writeMessage(Message msg) {
        try {
            this.objectOutputStream.writeObject(msg);
            this.objectOutputStream.flush();
            logger.info("Server sent message " + msg.getType() + " to " + loggedInUser);
        } catch (IOException e) {
            logger.warn("Exception thrown while writing to client");
        }
    }

    private Message readMessage() {
        try {
            Message msg = (Message) objectInputStream.readObject();
            logger.info("Server received a message from "  + loggedInUser + ": " + msg.getType());
            return msg;
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("Exception thrown while reading a message");
        }
        return null;
    }
    public void run() {
        Message msg;
        while (true) {
            msg = readMessage();
            if (msg == null) {
                close();
                return;
            }
            ChatManager instance = ChatManager.getInstance();
            switch (msg.getType()) {
                case LOGIN: {
                    String username = (String) msg.getData();
                    if (ChatManager.getInstance().login(username, this)) {
                        this.loggedInUser = username;
                        writeMessage(new Message(LOGIN_SUCCESS));
                        ChatManager.getInstance().updateOnlineUsers();
                    } else {
                        writeMessage(new Message(LOGIN_FAIL));
                    }
                    instance.updateOnlineChatRooms();
                    break;
                }
                case SEND_PRIVATE: {
                    String[] data = (String[]) msg.getData();
                    if (!instance.sendMessageToUser(
                            data[0], data[1], data[2])) {
                        writeMessage(new Message(SEND_PRIVATE_FAIL));
                    }
                    break;
                }
                case SEND_GROUP: {
                    String[] data = (String[]) msg.getData();
                    System.out.println(Arrays.toString(data));
                    instance.sendMessageInChatRoom(data[0], data[1], data[2]);
                    break;
                }
                case CREATE: {
                    String[] data = (String[]) msg.getData();
                    if (instance.createChatRoom(data[0], data[1])) {
                        String[] dataToSend = {data[0], data[1]};
                        writeMessage(new Message(CREATE_SUCCESS, dataToSend));
                        instance.updateOnlineChatRooms();
                        instance.updateJoinedUsersForChatRoom(data[1]);
                    } else {
                        writeMessage(new Message(CREATE_FAIL));
                    }
                    break;
                }
                case JOIN: {
                    String[] data = (String[]) msg.getData();
                    String username = data[0];
                    String chatRoomName = data[1];
                    try {
                        if (instance.joinChatRoom(username, chatRoomName)) {
                            String[] dataToSend = {ChatManager.getInstance().getChatRoomOwner(chatRoomName), chatRoomName};
                            writeMessage(new Message(JOIN_SUCCESS, dataToSend));
                            instance.updateJoinedUsersForChatRoom(data[1]);
                        } else {
                            logger.info("Client tried to join a non-existent chat room");
                            writeMessage(new Message(JOIN_FAIL, "Chat room does not exist"));
                        }
                    } catch (UserAlreadyInChatRoomException e) {
                        logger.info("Client failed to join the room");
                        writeMessage(new Message(JOIN_FAIL, "You already joined!"));
                    }
                    break;
                }
                case LEAVE_ROOM: {
                    String[] data = (String[]) msg.getData();
                    instance.removeUserFromChatRoom(data[0], data[1]);
                    instance.updateJoinedUsersForChatRoom(data[1]);
                    writeMessage(new Message(LEAVE_ROOM));
                    break;
                }
                case DELETE_ROOM: {
                    instance.deleteRoom((String) msg.getData());
                    instance.updateOnlineChatRooms();
                    break;
                }
                case QUIT:
                    instance.disconnectUser((String) msg.getData());
                    instance.updateOnlineUsers();
                    writeMessage(new Message(QUIT));
                    return;
            }
        }
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            logger.warn("Exception thrown while closing the socket");
        }
    }
}
