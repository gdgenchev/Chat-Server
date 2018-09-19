package bg.chat.client.command;

import bg.chat.client.Client;
import bg.chat.client.command.exceptions.NotLoggedInException;
import bg.chat.client.command.exceptions.WrongUsageException;

import java.util.List;

public class SendCommand extends Command {
    SendCommand(String command, List<String> data) {
        super(command, data);
    }

    @Override
    public void execute(Client client) throws WrongUsageException, NotLoggedInException {
        if (client.isLogged()) {
            if (lineData.size() >= 2) {
                String receiver = lineData.get(0);
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < lineData.size(); i++) {
                    if (i != lineData.size() - 1) {
                        message.append(lineData.get(i)).append(" ");
                    } else {
                        message.append(lineData.get(i));
                    }
                }
                client.writeLine("SEND " + client.getUsername() + " " + receiver + " " + message);
            } else {
                throw new WrongUsageException("Usage: " + command + " <username> <message>");
            }
        } else {
            throw new NotLoggedInException("Please log in before you send a message!");
        }
    }
}
