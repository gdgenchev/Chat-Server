package bg.chat.client.command;

import bg.chat.client.Client;
import bg.chat.client.command.exceptions.WrongUsageException;
import bg.chat.common.FileUtils;

import java.util.List;

public class RegisterCommand extends Command {
    RegisterCommand(String command, List<String> data) {
        super(command, data);
    }

    @Override
    public void execute(Client client) throws WrongUsageException {
        if (lineData.size() == 2) {
            FileUtils.register(lineData.get(0), lineData.get(1).toCharArray());
        } else {
            throw new WrongUsageException("Usage: " + command + " <username> <password>");
        }
    }
}
