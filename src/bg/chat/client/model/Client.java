package bg.chat.client.model;

import bg.chat.client.exceptions.FailedConnectionException;

import java.io.*;
import java.net.Socket;

public class Client implements Closeable {

    private Socket           socket;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;
    private String           username;

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

    public void writeLine(String line) throws IOException {
        this.objectOutputStream.writeObject(line);
        this.objectOutputStream.flush();
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        return this.objectInputStream.readObject();
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
}
