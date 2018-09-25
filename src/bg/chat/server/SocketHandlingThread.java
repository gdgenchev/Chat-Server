package bg.chat.server;

import bg.chat.common.Message;
import bg.chat.common.MsgState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import static bg.chat.common.MsgState.*;

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

    synchronized void writeMessage(Message msg) throws IOException {
        this.objectOutputStream.writeObject(msg);
        this.objectOutputStream.flush();
    }

    public void run() {
        Message msg;
        while (true) {
            try {
                msg = (Message) objectInputStream.readObject();
                if (msg == null) {
                    socket.close();
                    return;
                }
                System.out.println(msg.getState().toString() + " " + msg.getData());
                switch (msg.getState()) {
                    case LOGIN:
                        String username = (String) msg.getData();
                        if (ChatManager.getInstance().login(username, this)) {
                            writeMessage(new Message(LOGIN_SUCCESS));
                            ChatManager.getInstance().updateOnlineUsers();
                        } else {
                            writeMessage(new Message(LOGIN_FAIL));
                        }
                        break;
                    case SEND_PRIVATE: {
                        String[] data = (String[]) msg.getData();
                        if (!ChatManager.getInstance().sendMessageToUser(
                                data[0], data[1], data[2])) {
                            writeMessage(new Message(SEND_PRIVATE_FAIL));
                        }
                        break;
                    }
                    case CREATE: {
                        String[] data = (String[]) msg.getData();
                        ChatManager.getInstance().createChatRoom(data[0], data[1]);
                        ChatManager.getInstance().updateOnlineChatRooms();
                        break;
                    }
                    case QUIT:
                        ChatManager.getInstance().disconnectUser((String) msg.getData());
                        ChatManager.getInstance().updateOnlineUsers();
                        writeMessage(new Message(QUIT));
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
