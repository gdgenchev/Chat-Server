package bg.chat.client;

import bg.chat.common.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client extends Thread {
    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;
    private static Semaphore s1;

    private Client() {
        s1 = new Semaphore(1);
    }

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

    public void run () {
        while (true) {
            try {
                String line = in.readLine();
                String[] lineData = line.split(" ");
                String cmd = lineData[0];
                if (line.equalsIgnoreCase("list-users 0")) {
                    boolean has = false;
                    while(!line.equalsIgnoreCase("list-users 1")) {
                        line = in.readLine();
                        if (!line.equalsIgnoreCase("list-users 1")) {
                            if (!has) {
                                System.out.println("Connected users:");
                            }
                            System.out.println(line);
                            has = true;
                        }
                    }
                    if (!has) {
                        System.out.println("No online users!");
                    }
                    s1.release();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                String commandLine = sc.nextLine();
                String[] parts = commandLine.split(" ");
                String cmd = parts[0];
                switch (cmd) {
                    case "connect":
                        if (parts.length == 3) {
                            client.connectToServer(parts[1], Integer.parseInt(parts[2]));
                            client.start();
                            System.out.println("Successfully connected to server");
                        } else {
                            System.out.println("Usage: connect <host> <port>");
                        }
                        break;
                    case "register":
                        if (parts.length == 3) {
                            new User(parts[1], parts[2]).register();
                        } else {
                            System.out.println("Usage: register <username> <password>");
                        }
                        break;
                    case "login":
                        if (parts.length == 3) {
                            if (new User(parts[1], parts[2]).isRegistered()) {
                                System.out.println("Successfully logged in!");
                                client.writeLine("LOGIN " + parts[1]);
                            }
                        } else {
                            System.out.println("Usage: login <username> <password>");
                        }
                        break;
                    case "list-users":
                        //TODO: add support for list-users chatRoom
                        client.writeLine("LIST-USERS");
                        s1.acquire();
                        break;
                    case "send":
                        if (parts.length == 3) {
                            client.writeLine("SEND " + parts[1] + " " + parts[2]);
                        }
                        break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
