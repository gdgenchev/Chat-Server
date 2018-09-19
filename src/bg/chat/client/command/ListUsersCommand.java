package bg.chat.client.command;

import bg.chat.client.Client;

import java.util.List;

public class ListUsersCommand extends Command {

    ListUsersCommand(String command, List<String> data) {
        super(command, data);
    }

    @Override
    public void execute(Client client) {
        client.writeLine(command);
        client.blockMainThread();
    }
}
