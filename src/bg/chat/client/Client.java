package bg.chat.client;

import bg.chat.common.FileWriterUtils;

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
                String[] lineData = commandLine.split(" ");
                String cmd = lineData[0];
                switch (cmd) {
                    case "connect":
                        if (lineData.length == 3) {
                            client.connectToServer(lineData[1], Integer.parseInt(lineData[2]));
                            client.start();
                            System.out.println("Successfully connected to server");
                        } else {
                            System.out.println("Usage: connect <host> <port>");
                        }
                        break;
                    case "register":
                        if (lineData.length == 3) {
                            FileWriterUtils.register(lineData[1], lineData[2].toCharArray());
                        } else {
                            System.out.println("Usage: register <username> <password>");
                        }
                        break;
                    case "login":
                        if (lineData.length == 3) {
                            if (FileWriterUtils.isRegistered(lineData[1], lineData[2].toCharArray())) {
                                System.out.println("Successfully logged in!");
                                client.writeLine("LOGIN " + lineData[1]);
                               // FileWriterUtils.registerUserToFile(username, password);
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
                        if (lineData.length == 3) {
                            client.writeLine("SEND " + lineData[1] + " " + lineData[2]);
                        }
                        break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
