package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.common.Message;

import java.util.HashSet;
import java.util.Set;

public class ReceiverBroadcaster extends Thread {
    private Client client;
    private Set<Receiver> receivers;

    ReceiverBroadcaster(Client client) {
        this.client = client;
        receivers = new HashSet<>();
    }

    void addReceiver(Receiver receiver) {
        this.receivers.add(receiver);
    }

    private void broadcastMessage(Message msg) {
        for (Receiver receiver : receivers) {
            receiver.respondToMessage(msg);
        }
    }

    @Override
    public void run() {
        while (true) {
            broadcastMessage(client.readMessage());
        }
    }
}
