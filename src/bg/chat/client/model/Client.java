package bg.chat.client.model;

import bg.chat.client.exceptions.FailedConnectionException;
import bg.chat.common.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Client implements Closeable {

    private Socket socket;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;
    private String username;
    private String chatRoom;
    private boolean connected;
    private final static Logger logger = LogManager.getLogger("Client Logger");

    public Client() {
        username = null;
    }

    public void connectToServer(String host, int port) throws FailedConnectionException {
        try {
            this.socket = new Socket(host, port);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            setConnected(true);
            logger.info(username + " connected to server");
        } catch (IOException e) {
            logger.warn(username + " failed to connect to server");
            throw new FailedConnectionException("Connection Error");
        }
    }

    public void writeMessage(Message msg) {
        try {
            this.objectOutputStream.writeObject(msg);
            this.objectOutputStream.flush();
            logger.info(username + " sent: " + msg.getType());
        } catch (IOException e) {
            logger.warn("Exception thrown while writing to server");
        }
    }

    public Message readMessage() {
        try {
            Message msg = (Message) this.objectInputStream.readObject();
            logger.info(username + " read: " + msg.getType());
            return msg;
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("Exception thrown while reading a message from server");
        }
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void close() {
        try {
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
            logger.info("Client socket closed");
        } catch (IOException e) {
            logger.error("Exception thrown while closing the client socket");
        }
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getChatRoom() {
        return this.chatRoom;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
