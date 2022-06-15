package main.transmission;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ListeningThread extends Thread {
    private SocketServer socketServer;
    private ServerSocket serverSocket;
    private Vector<ConnectionThread> connectionThreads;
    private Vector<ConnectionThread> notRunningConnectionThreads;
    private boolean isRunning;

    public ListeningThread(SocketServer socketServer, ServerSocket serverSocket) {
        this.socketServer = socketServer;
        this.serverSocket = serverSocket;
        this.connectionThreads = new Vector<ConnectionThread>();
        this.notRunningConnectionThreads = new Vector<ConnectionThread>();
        isRunning = true;
    }

    @Override
    public void run() {
        while(isRunning) {
            if (serverSocket.isClosed()) {
                isRunning = false;
                break;
            }

            removeClosedConnection();

            acceptConnection();
        }
    }

    private void removeClosedConnection() {
        // Remove not running connection threads.
        for (ConnectionThread connectionThread : connectionThreads) {
            if (!connectionThread.isRunning()) {
                notRunningConnectionThreads.addElement(connectionThread);
            }
        }
        for (ConnectionThread connectionThread : notRunningConnectionThreads) {
            connectionThreads.remove(connectionThread);
        }
        notRunningConnectionThreads.clear();
    }

    private void acceptConnection() {
        try {
            Socket socket;
            socket = serverSocket.accept();
            ConnectionThread connectionThread = new ConnectionThread(socket, socketServer);
            connectionThreads.addElement(connectionThread);
            connectionThread.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopRunning() {
        for (int i = 0; i < connectionThreads.size(); i++)
            connectionThreads.elementAt(i).stopRunning();
        isRunning = false;
    }

    public static class Connection {
        private Socket socket;

        public Connection(Socket socket) {
            this.socket = socket;
        }

        public void println(String message) {
            PrintWriter writer;
            try {
                writer = new PrintWriter(new OutputStreamWriter(
                                         socket.getOutputStream()), true);
                writer.println(message);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}