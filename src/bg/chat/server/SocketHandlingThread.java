package bg.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

public class SocketHandlingThread extends Thread {
    private final Socket socket;


    private DataOutputStream out = null;

    SocketHandlingThread(Socket socket) {
        this.socket = socket;
    }

    public void writeLine(String line) throws IOException {
        this.out.writeBytes(line + "\n");
        out.flush();
    }

    public void run() {
        InputStream inp;
        BufferedReader brinp;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null)) {
                    socket.close();
                    return;
                }
                String[] lineData = line.split(" ");
                String cmd = lineData[0];
                switch (cmd) {
                    case "LOGIN":
                        String username = lineData[1];
                        if (ChatManager.getInstance().login(username, this)) {
                            writeLine("LOGIN 1");
                        } else {
                            writeLine("LOGIN 2");
                        }
                        break;
                    case "LIST-USERS":
                        Collection<String> usernames = ChatManager.getInstance().getAllUsernames();
                        sendUsersList(usernames);
                        break;
                    case "SEND":
                        if (!ChatManager.getInstance().sendMessageToUser(lineData[1],
                                lineData[2], line.substring(cmd.length() + lineData[1].length() + lineData[2].length() + 3))) {
                            writeLine("SEND 2");
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendUsersList(Collection<String> usernames) throws IOException {
        writeLine("LIST-USERS 0");
        for (final String username : usernames) {
            writeLine(username);
        }
        writeLine("LIST-USERS 1");
    }

}
