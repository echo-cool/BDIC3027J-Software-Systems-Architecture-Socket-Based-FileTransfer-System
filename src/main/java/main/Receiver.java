package main;

import main.socket.SocketServer;
import main.socket.handlers.FileHandler;

import java.io.*;

public class Receiver {
    private SocketServer socketServer;

    public Receiver(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    public static void WriteToFile(String fileName, byte[] data) {
        OutputStream out = null;
//        System.out.println(Arrays.toString(data));
        try {
            out = new FileOutputStream("src/main/resources/receive/" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void WriteToFile(String fileName, byte[] data, String prefix) {
        OutputStream out = null;
//        System.out.println(Arrays.toString(data));
        try {
            out = new FileOutputStream("src/main/resources/receive/" + prefix + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver(new SocketServer(Config.SERVER_PORT, new FileHandler()));
    }
}
