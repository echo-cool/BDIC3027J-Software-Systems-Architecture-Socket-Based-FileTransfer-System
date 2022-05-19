package main.socket.handlers;

import main.socket.connection.Connection;

public class PrintHandler implements IMessageHandler {
    @Override
    public void onReceive(Connection connection, String message) {
        System.out.println("Got a message from a client:");
        System.out.println(message);
    }
}