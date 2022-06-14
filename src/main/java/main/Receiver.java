package main;

import main.socket.SocketClient;
import main.socket.SocketServer;
import main.socket.handlers.FileHandler;
import main.task.TaskReceive;
import main.task.TaskSend;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Receiver {
    private SocketServer socketServer;
    private Scanner sc;
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

    private void displayTasks(){
        System.out.println("Current Tasks:");
        for (TaskReceive taskReceive : FileHandler.taskHashMap.values()) {
            System.out.println(taskReceive);
        }
    }

    private void showPrompt(){
//        displayTasks();

        System.out.print(">>> ");
    }

    public void inputManager(){
        sc = new Scanner(System.in);
        displayTasks();
        showPrompt();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            switch (line) {
                case "q":
                    //Query task
                    displayTasks();
                    for (TaskReceive t : FileHandler.taskHashMap.values()) {
                        for(int i = 0; i < t.getFiles().size(); i++){
                            int total = t.getFiles().get(i).getTotalSegments();
                            int sent = t.getFiles().get(i).getMessages().size();
                            System.out.print("Task-" + t.getTaskID() + " FileID-" + i + ": " + String.format("%.2f",(((double)sent)/total)*100) +"%  "+ sent + "/" + total);
                            System.out.println();
                        }
                    }
                    break;
                case "pause":
                    //Pause task
                    System.out.println("Which task do you want to pause: ");
                    displayTasks();
                    System.out.print("Enter task ID: ");
                    int taskID = sc.nextInt();
                    if(!FileHandler.taskHashMap.containsKey(taskID)|| taskID < 0){
                        System.out.println("Invalid task ID");
                        System.out.println("Task " + taskID + " does not exist");
                        break;
                    }
                    TaskReceive task = FileHandler.taskHashMap.get(taskID);
                    if(task != null){
                        task.setPaused(true);
                    }
                    System.out.println("Task " + taskID + " paused");

                    break;

                case "resume":
                    //Resume task
                    System.out.println("Which task do you want to resume: ");
                    displayTasks();
                    System.out.print("Enter task ID: ");
                    taskID = sc.nextInt();
                    if(!FileHandler.taskHashMap.containsKey(taskID)|| taskID < 0){
                        System.out.println("Invalid task ID");
                        System.out.println("Task " + taskID + " does not exist");
                        break;
                    }
                    TaskReceive taskReceive = FileHandler.taskHashMap.get(taskID);
                    if(taskReceive != null){
                        taskReceive.setPaused(false);
                    }
                    System.out.println("Task " + taskID + " resumed");
                    break;

            }
            showPrompt();
        }
    }

    public static void main(String[] args) {
        Receiver receiver = new Receiver(new SocketServer(Config.SERVER_PORT, new FileHandler()));
        receiver.inputManager();
    }
}
