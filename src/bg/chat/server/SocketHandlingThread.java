package bg.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandlingThread extends Thread {
    private final Socket       socket;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream  objectInputStream = null;

    SocketHandlingThread(Socket socket) {
        this.socket = socket;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void writeObject(Object obj) throws IOException {
        this.objectOutputStream.writeObject(obj);
        this.objectOutputStream.flush();
    }

    public void run() {
        String line;
        while (true) {
            try {
                line = (String)objectInputStream.readObject();
                if (line == null) {
                    socket.close();
                    return;
                }
                System.out.println(line);
                String[] lineData = line.split(" ");
                String cmd = lineData[0];
                switch (cmd) {
                    case "LOGIN":
                        String username = lineData[1];
                        if (ChatManager.getInstance().login(username, this)) {
                            writeObject("LOGIN 1");
                            ChatManager.getInstance().updateOnlineUsers();
                        } else {
                            writeObject("LOGIN 2");
                        }
                        break;
                    case "SEND":
                        if (!ChatManager.getInstance().sendMessageToUser(
                                lineData[1], lineData[2],
                                line.substring(
                                        cmd.length()
                                                + lineData[1].length()
                                                + lineData[2].length() + 3))) {
                            writeObject("SEND 2");
                        }
                        break;
                    case "QUIT":
                        ChatManager.getInstance().disconnectUser(lineData[1]);
                        ChatManager.getInstance().updateOnlineUsers();
                        writeObject("QUIT");
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                //Assume client disconnected
                //Already handled disconnection from QUIT
                return;
           }
        }
    }

}
