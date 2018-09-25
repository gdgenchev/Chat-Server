package bg.chat.client.controller;

import bg.chat.common.Message;

public interface Receiver {

    /**
     * Updates the corresponding view(PrivateChat/GroupChat)
     * according to the message type received
     * @param msg The message received from the server
     */
    void respondToMessage(Message msg);
}
