package bg.chat.client.command;

import bg.chat.client.Client;
import bg.chat.client.command.exceptions.AlreadyConnectedException;
import bg.chat.client.command.exceptions.NotLoggedInException;
import bg.chat.client.command.exceptions.WrongUsageException;

import java.util.List;

public abstract class Command {

    List<String> lineData;

    String command;

    Command(String command, List<String> data) {
        this.command = command;
        this.lineData = data;
    }

    public List<String> getLineData() {
        return lineData;
    }

    public abstract void execute(Client client) throws WrongUsageException, NotLoggedInException, AlreadyConnectedException;
}
