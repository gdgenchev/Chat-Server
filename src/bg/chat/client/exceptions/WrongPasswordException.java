package bg.chat.client.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String s) {
        super(s);
    }
}
