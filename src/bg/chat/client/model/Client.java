package bg.chat.client.model;

import bg.chat.client.exceptions.FailedConnectionException;

import java.io.*;
import java.net.Socket;

public class Client implements Closeable {

    private Socket           socket;
    private DataOutputStream out;
    private BufferedReader   brinp;
    private String           username;

    public Client() {
        username  = null;
    }

    public  void connectToServer(String host, int port) throws FailedConnectionException {
        try {
            this.socket = new Socket(host,port);
            this.out    = new DataOutputStream(socket.getOutputStream());
            this.brinp  = new BufferedReader(new InputStreamReader(
                              socket.getInputStream()));
        } catch (IOException e) {
            throw new FailedConnectionException("Connection Error");
        }
    }

    public void writeLine(String line) throws IOException {
        this.out.writeBytes(line + "\n");
        out.flush();
    }

    public String readLine() throws IOException {
        return this.brinp.readLine();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void close() throws IOException {
        brinp.close();
        out.close();
        socket.close();
    }
}
