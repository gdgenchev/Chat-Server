package bg.chat.server;

class User {
    private SocketHandlingThread socket;
    private String username;

    User(String username, SocketHandlingThread socket) {
        this.socket = socket;
        this.username = username;
    }

    SocketHandlingThread getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }
}