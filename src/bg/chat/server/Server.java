package bg.chat.server;

import bg.chat.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        System.out.println("Creating server socket on port " + Constants.PORT);
        try(ServerSocket listener = new ServerSocket(Constants.PORT)) {
            while (true) {
                Socket clientSocket = listener.accept();
                new SocketHandlingThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}