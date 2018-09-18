package bg.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends Thread {
    private final Socket socket;

    private static Set<String> connectedUsernames = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private DataOutputStream out = null;

    void login(String username) {
        connectedUsernames.add(username);
        System.out.println("User " + username + " added!");
    }

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
                String[] parts = line.split(" ");
                String command = parts[0];
                switch (command) {
                    case "LOGIN":
                        login(parts[1]);
                        break;
                    case "LIST-USERS":
                        writeLine(connectedUsernames.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
