package bg.chat.client;

import bg.chat.common.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;

    private void connectToServer(String host, int port) {
        try {
            this.socket = new Socket(host,port);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLine(String line) throws IOException {
        this.out.writeBytes(line + "\n");
        out.flush();
    }

    private void start() {
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                String commandLine = sc.nextLine();
                String[] parts = commandLine.split(" ");
                String command = parts[0];
                switch (command) {
                    case "connect":
                        connectToServer(parts[1], Integer.parseInt(parts[2]));
                        break;
                    case "register":
                        new User(parts[1], parts[2]).register();
                        break;
                    case "login":
                        if (new User(parts[1], parts[2]).isRegistered()) {
                            System.out.println("Successfully logged in!");
                            writeLine("LOGIN " + parts[1]);
                        }
                        break;
                    case "list-users":
                        writeLine("LIST-USERS");
                        try {
                            String users = in.readLine();
                            System.out.println(users);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
