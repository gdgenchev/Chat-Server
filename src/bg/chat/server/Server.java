package bg.chat.server;

import bg.chat.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final Logger logger = LogManager.getLogger("Server Accepting Logger");
    public static void main(String[] args) {
        System.out.println("Creating server socket on port " + Constants.PORT);
        try(ServerSocket listener = new ServerSocket(Constants.PORT)) {
            while (true) {
                Socket clientSocket = listener.accept();
                new SocketHandlingThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("Exception thrown while accepting a client", e);
        }
    }
}