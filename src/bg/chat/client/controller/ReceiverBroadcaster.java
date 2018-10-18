package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.common.Message;

import java.util.*;

public class ReceiverBroadcaster extends Thread {
    private Client client;
    private List<Receiver> receivers;

    ReceiverBroadcaster(Client client) {
        this.client = client;
        receivers = new ArrayList<>();
    }

    void addReceiver(Receiver receiver) {
        this.receivers.add(receiver);
    }

    private void broadcastMessage(Message msg) {
        for (int i = 0; i < receivers.size(); i++) {
            receivers.get(i).respondToMessage(msg);
        }
    }

    @Override
    public void run() {
        while (client.isConnected()) {
            broadcastMessage(client.readMessage());
        }
    }
}
