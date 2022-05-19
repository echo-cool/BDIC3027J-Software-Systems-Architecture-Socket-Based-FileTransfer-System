package main.socket.example;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import main.Config;
import main.socket.SocketClient;
import main.socket.SocketServer;
import main.socket.handlers.EchoHandler;

class SocketExample {
    private static Scanner inputScanner = new Scanner(System.in);

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        // Start as a server or a client.
        System.out.println("Please input '0' or '1' to start a server or a client.");
        System.out.println("Before starting a client, make sure a server is already running at the same PC.");
        System.out.println("  0: server");
        System.out.println("  1: client");
        System.out.println("  others: close the program.");
        String input = inputScanner.next();
        switch (input) {
        case "0":
            startServer();
            break;
        case "1":
            startClient();
            break;
        default:
            break;
        }
        inputScanner.close();
    }

    private static void startServer() {
        System.out.println("Start a server.");
        SocketServer server = new SocketServer(Config.SERVER_PORT, new EchoHandler());

        System.out.println("Please type anything and press enter to close the server...");
        inputScanner.next();
        server.close();
    }

    private static void startClient() throws UnknownHostException {
        System.out.println("Start a client.");
        SocketClient client = new SocketClient(InetAddress.getLocalHost(), Config.SERVER_PORT);

        System.out.println("Please type something to send to the server...");
        String string = inputScanner.next();
        client.println(string);

        System.out.println("Got the following message from the server:");
        System.out.println(client.readLine());

        System.out.println("Please type anything and press enter to close the client...");
        inputScanner.next();
        client.close();
    }
}