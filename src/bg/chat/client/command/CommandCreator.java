package bg.chat.client.command;

import java.util.List;

public class CommandCreator {

    public static Command create(CommandType cmdType, List<String> lineData) {
        switch (cmdType) {
            case CONNECT:
                return new ConnectCommand(cmdType.getText(), lineData);
            case LOGIN:
                return new LoginCommand(cmdType.getText(), lineData);
            case REGISTER:
                return new RegisterCommand(cmdType.getText(), lineData);
            case LIST_USERS:
                return new ListUsersCommand(cmdType.getText(), lineData);
            case SEND:
                return new SendCommand(cmdType.getText(), lineData);
            default:
                return null;
        }
    }
}
