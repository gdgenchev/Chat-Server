package bg.chat.client;

import bg.chat.client.command.Command;
import bg.chat.client.command.CommandCreator;
import bg.chat.client.command.CommandType;
import bg.chat.client.command.exceptions.AlreadyConnectedException;
import bg.chat.client.command.exceptions.NotLoggedInException;
import bg.chat.client.command.exceptions.WrongUsageException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client extends Thread {
    private Socket socket;

    private DataOutputStream out;

    private BufferedReader brinp;

    private static Semaphore s1;

    private  String username;

    private boolean connected;

    private boolean logged;

    private Client() {
        connected = false;
        logged = false;
        username = null;
        s1 = new Semaphore(1);
    }

    public void connectToServer(String host, int port) {
        try {
            this.socket = new Socket(host,port);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.brinp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            setConnected(true);
            this.start();
            System.out.println("Successfully connected to server!");
        } catch (IOException e) {
            System.out.println("Failed to connect to server!");
        }
    }

    public void writeLine(String line) {
        try {
            this.out.writeBytes(line + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isLogged() {
        return logged;
    }

    public void blockMainThread() {
        try {
            s1.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                    if (state == 1) {
                        String from = lineData[2];
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
        while (true) {
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            List<String> lineData = new ArrayList<>(Arrays.asList(line.split(" ")));
            String cmd = lineData.get(0);
            lineData.remove(0);
            CommandType cmdType = CommandType.fromString(cmd);
            Command command = CommandCreator.create(cmdType, lineData);
            try {
                if (command != null) {
                    command.execute(client);
                }
            } catch (WrongUsageException | NotLoggedInException | AlreadyConnectedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
