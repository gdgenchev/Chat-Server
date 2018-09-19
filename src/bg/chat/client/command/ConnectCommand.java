package bg.chat.client.command;

import bg.chat.client.Client;
import bg.chat.client.command.exceptions.AlreadyConnectedException;

import java.util.List;

public class ConnectCommand extends Command {

    ConnectCommand(String command, List<String> data) {
        super(command, data);
    }

    @Override
    public void execute(Client client) throws AlreadyConnectedException {
        if (client.isConnected()) {
            throw new AlreadyConnectedException("Already connected!");
        } else if (lineData.size() != 2) {
            System.out.println("Usage: " + command + " <host> <port>");
            return;
        }
        client.connectToServer(lineData.get(0), Integer.parseInt(lineData.get(1)));
    }
}
