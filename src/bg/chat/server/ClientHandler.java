package bg.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;

public class ClientHandler extends Thread {
    private final Socket socket;


    private DataOutputStream out = null;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private void writeLine(String line) throws IOException {
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
                System.out.println(Arrays.toString(lineData));
                switch (cmd) {
                    case "LOGIN":
                        ChatManager.getInstance().login(lineData[1]);
                        break;
                    case "LIST-USERS":
                        Collection<String> usernames = ChatManager.getInstance().getAllUsers();
                        sendUsersList(usernames);
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
