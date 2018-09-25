package bg.chat.client.model;

import bg.chat.client.exceptions.FailedConnectionException;
import bg.chat.common.Message;

import java.io.*;
import java.net.Socket;

public class Client implements Closeable {

    private Socket             socket;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream  objectInputStream = null;
    private String             username;
    private String             chatRoomName;

    public Client() {
        username  = null;
    }

    public  void connectToServer(String host, int port) throws FailedConnectionException {
        try {
            this.socket = new Socket(host,port);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new FailedConnectionException("Connection Error");
        }
    }

    public void writeMessage(Message msg) throws IOException {
        this.objectOutputStream.writeObject(msg);
        this.objectOutputStream.flush();
    }

    public Message readMessage() throws ClassNotFoundException, IOException {
        return (Message)this.objectInputStream.readObject();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void close() throws IOException {
       objectOutputStream.close();
       objectInputStream.close();
       socket.close();
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }
}
