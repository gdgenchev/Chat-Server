package bg.chat.client.command.exceptions;

public class AlreadyConnectedException extends Exception {
    public AlreadyConnectedException(String message) {
        super(message);
    }
}
