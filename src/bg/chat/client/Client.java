package bg.chat.client;

import bg.chat.common.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client extends Thread {
    private Socket socket;

    private DataOutputStream out;

    private BufferedReader brinp;

    private static Semaphore s1;

    private  String username;

    private Client() {
        s1 = new Semaphore(1);
    }

    private void connectToServer(String host, int port) {
        try {
            this.socket = new Socket(host,port);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.brinp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLine(String line) throws IOException {
        this.out.writeBytes(line + "\n");
        out.flush();
    }

    public void run () {
        try {
            while (true) {
                String line = brinp.readLine();
                String[] lineData = line.split(" ");
                String cmd = lineData[0];
                int state = Integer.parseInt(lineData[1]);
                if (cmd.equalsIgnoreCase("login")) {
                    if (state == 1) {
                        System.out.println("Successfully logged in!");
                    } else if (state == 2) {
                        System.out.println("You are already logged in from somewhere!");
                    }
                } else if (line.equalsIgnoreCase("list-users 0")) {
                    boolean has = false;
                    while(!line.equalsIgnoreCase("list-users 1")) {
                        line = brinp.readLine();
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
                } else if (cmd.equalsIgnoreCase("send")) {
                    String from = lineData[2];
                    if (state == 1) {
                        String receivedText = line.substring(cmd.length() + from.length() + 4);
                        String message = "From: " + from
                                + "\n" + "Message: " + receivedText;
                        System.out.println(message);
                    } else if (state == 2) {
                        System.out.println("The user is not online");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        boolean connected = false;
        boolean logged = false;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                String line = sc.nextLine();
                String[] lineData = line.split(" ");
                String cmd = lineData[0];
                switch (cmd) {
                    case "connect":
                        if (connected) {
                            System.out.println("Already connected!");
                        } else if (lineData.length == 3) {
                            client.connectToServer(lineData[1], Integer.parseInt(lineData[2]));
                            client.start();
                            System.out.println("Successfully connected to server!");
                            connected = true;
                        } else {
                            System.out.println("Usage: connect <host> <port>");
                        }
                        break;
                    case "register":
                        if (lineData.length == 3) {
                            FileUtils.register(lineData[1], lineData[2].toCharArray());
                        } else {
                            System.out.println("Usage: register <username> <password>");
                        }
                        break;
                    case "login":
                        if (lineData.length == 3) {
                            if (FileUtils.isRegistered(lineData[1], lineData[2].toCharArray())) {
                                client.username = lineData[1];
                                client.writeLine("LOGIN " + client.username);
                                logged = true;
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
                        if (logged) {
                            String receiver = lineData[1];
                            String message = line.substring(cmd.length() + lineData[1].length() + 2);
                            if (lineData.length >= 3) {
                                client.writeLine("SEND " + client.username + " " + receiver + " " + message);
                            }
                        } else {
                            System.out.println("You can't send messages before login!");
                        }
                        break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
