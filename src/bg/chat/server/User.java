package bg.chat.server;

public class User {
    private SocketHandlingThread socket;

    private String username;

    public User( String username, SocketHandlingThread socket) {
        this.socket = socket;
        this.username = username;
    }

    public SocketHandlingThread getSocket() {
        return socket;
    }

}
