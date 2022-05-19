package main.socket.handlers;

import main.socket.connection.Connection;

public interface IMessageHandler {
    public void onReceive(Connection connection, String message);
}