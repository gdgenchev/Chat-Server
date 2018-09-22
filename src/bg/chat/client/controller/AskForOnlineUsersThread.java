package bg.chat.client.controller;

import bg.chat.client.model.Client;

import java.io.IOException;

class AskForOnlineUsersThread extends Thread {
    private Client client;

    AskForOnlineUsersThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                client.writeLine("LIST-USERS");
                sleep(1500);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
