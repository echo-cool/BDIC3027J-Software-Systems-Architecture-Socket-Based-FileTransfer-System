package main.transmission;

public interface IMessageHandler {
    public void onReceive(ListeningThread.Connection connection, String message);
}