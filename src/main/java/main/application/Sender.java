package main.application;

import main.file.AbstractPartitioner;
import main.transmission.SocketClient;
import main.transmission.TaskSend;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Sender {
    private ArrayList<TaskSend> taskSends;
    private Scanner sc;

    public Sender() {
        this.taskSends = new ArrayList<>();
    }


    private void addFileTest(){
        File file = new File("src/main/resources/test.jpg");
        File file2 = new File("src/main/resources/test.txt");
        File file3 = new File("src/main/resources/COMP3030J_Spring_2021-2022_Group_8.zip");
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        files.add(file2);
        ArrayList<File> files1 = new ArrayList<>();
        files1.add(file3);
        taskSends.add(new TaskSend(0, files));
        ArrayList<File> files2 = (ArrayList<File>) files.clone();
        files2.add(file2);
        taskSends.add(new TaskSend(1, files2));
//        taskSends.add(new TaskSend(2, files1));
//        taskSends.add(new TaskSend(3, files1));
//        taskSends.add(new TaskSend(4, files1));
//        taskSends.add(new TaskSend(5, files1));
//        taskSends.add(new TaskSend(6, files1));
    }

    public TaskSend createNewTask(){
        TaskSend taskSend = new TaskSend(taskSends.size());
        taskSends.add(taskSend);
        return taskSend;
    }

    public void sendFileTest(){


        for (TaskSend taskSend : taskSends) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SocketClient socketClient = new SocketClient(InetAddress.getLocalHost(), Config.SERVER_PORT);
                        taskSend.start(socketClient);
                        socketClient.close();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Monitor the tasks
                int prev_sent = 1;
                long prev_time = System.currentTimeMillis() - 1;

                while (true) {
                    int total_sent = 0;
                    for (TaskSend taskSend : taskSends) {
                        for(int i = 0; i < taskSend.getProgress().size(); i++){
                            int total = taskSend.getTotalSegment().get(i);
                            int sent = taskSend.getProgress().get(i);
                            System.out.print("Task-" + taskSend.getTaskID() + " FileID-" + i + ": " + String.format("%.2f",(((double)sent)/total)*100) +"%  "+ sent + "/" + total);
                            System.out.println();
                            total_sent += sent;
                        }
                    }
                    long current = System.currentTimeMillis();
//                    System.out.println(current - prev_time);
                    System.out.println("Speed: " + String.format("%.2f",((((double)total_sent) - prev_sent)/((current - prev_time)/1000.0))/(1024.0/ AbstractPartitioner.partitionSize)) + " KB/s");
                    prev_time = current;
                    prev_sent = total_sent;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for(int i = 0; i < 1000; i++)
                    {
                        System.out.println("\b");
                    }
                    System.out.flush();
                }
            }
        }).start();
    }

    private void displayTasks(){
        System.out.println("Current Tasks:");
        for (TaskSend taskSend : taskSends) {
            System.out.println(taskSend);
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
//                case "\n":
//                    //Promote
//                    showPrompt();
//                    break;
                case "new":
                    //Create new task
                    TaskSend taskSend = createNewTask();
                    System.out.println("Task " + taskSend.getTaskID() + " created");
                    displayTasks();
                    break;
                case "add":
                    //Add file to task
                    addFilesToTask();
                    displayTasks();
                    break;
                case "q":
                    //Query task
                    displayTasks();
                    queryTask();
                    break;
                case "start":
                    //Start task
                    System.out.println("Which task do you want to start: ");
                    displayTasks();
                    System.out.print("Enter task ID: ");
                    int taskID = sc.nextInt();
                    if(taskID > taskSends.size() || taskID < 0){
                        System.out.println("Invalid task ID");
                        System.out.println("Task " + taskID + " does not exist");
                        break;
                    }
                    startTask(taskID);
                    System.out.println("Task " + taskID + " started");
                    break;
                case "startall":
                    //Start all tasks
                    for (TaskSend t : taskSends) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    SocketClient socketClient = new SocketClient(InetAddress.getLocalHost(), Config.SERVER_PORT);
                                    t.start(socketClient);
                                    socketClient.close();
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    break;
                case "pause":
                    //Pause task
                    System.out.println("Which task do you want to pause: ");
                    displayTasks();
                    System.out.print("Enter task ID: ");
                    taskID = sc.nextInt();
                    if(taskID > taskSends.size() || taskID < 0){
                        System.out.println("Invalid task ID");
                        System.out.println("Task " + taskID + " does not exist");
                        break;
                    }
                    pauseTask(taskID);
                    break;

                case "resume":
                    //Resume task
                    System.out.println("Which task do you want to resume: ");
                    displayTasks();
                    System.out.print("Enter task ID: ");
                    taskID = sc.nextInt();
                    if(taskID > taskSends.size() || taskID < 0){
                        System.out.println("Invalid task ID");
                        System.out.println("Task " + taskID + " does not exist");
                        break;
                    }
                    resumeTask(taskID);
                    break;

            }
            showPrompt();
        }
    }

    private void resumeTask(int taskID) {
        taskSends.get(taskID).pause(false);
        System.out.println("Task " + taskID + " resumed");
    }

    private void pauseTask(int taskID) {
        taskSends.get(taskID).pause(true);
        System.out.println("Task " + taskID + " paused");
    }

    private void startTask(int taskID) {
        TaskSend t1 = taskSends.get(taskID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SocketClient socketClient = new SocketClient(InetAddress.getLocalHost(), Config.SERVER_PORT);
                    t1.start(socketClient);
                    socketClient.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void queryTask() {
        for (TaskSend t : taskSends) {
            for(int i = 0; i < t.getProgress().size(); i++){
                int total = t.getTotalSegment().get(i);
                int sent = t.getProgress().get(i);
                System.out.print("Task-" + t.getTaskID() + " FileID-" + i + ": " + String.format("%.2f",(((double)sent)/total)*100) +"%  "+ sent + "/" + total);
                System.out.println();
            }
        }
    }

    private void addFilesToTask() {
        System.out.println("You are adding a file to a task.");
        System.out.print("Enter task ID (default: 0): ");
        int taskID = sc.nextInt();
        if(taskID > taskSends.size() || taskID < 0){
            System.out.println("Invalid task ID");
            System.out.println("Task " + taskID + " does not exist");
            System.out.println("Using default task 0");
            taskID = 0;
        }
        sc.nextLine();
        System.out.print("Enter file path (default: src/main/resources/test.jpg): ");
        String filePath = sc.nextLine();
        if (filePath.equals("")) {
            filePath = "src/main/resources/test.jpg";
        }
        File file = new File(filePath);
        if(file.exists()){
            taskSends.get(taskID).addFile(file);
            System.out.println("File added to task " + taskID);
        }
        else{
            System.out.println("File does not exist");
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Sender sender = new Sender();
        sender.addFileTest();
        sender.inputManager();
        
    }
}
