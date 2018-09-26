package bg.chat.client.controller;

import bg.chat.client.model.Client;
import bg.chat.common.Message;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReceiverBroadcaster extends Thread {
    private Client client;
    private Set<Receiver> receivers;

    ReceiverBroadcaster(Client client) {
        this.client = client;
        receivers = Collections.synchronizedSet(new HashSet<>());
    }

    synchronized void addReceiver(Receiver receiver) {
        this.receivers.add(receiver);
    }

    synchronized private void broadcastMessage(Message msg) {
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
