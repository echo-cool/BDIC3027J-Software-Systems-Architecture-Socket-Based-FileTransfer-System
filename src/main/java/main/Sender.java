package main;

import main.partition.Partitioner;
import main.socket.SocketClient;
import main.task.TaskSend;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Sender {
    private ArrayList<TaskSend> taskSends;

    public Sender() {
        this.taskSends = new ArrayList<>();
    }

    public void sendFileTest(){
        File file = new File("src/main/resources/test.jpg");
        File file2 = new File("src/main/resources/test.txt");
        File file3 = new File("src/main/resources/COMP3030J_Spring_2021-2022_Group_8.zip");
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        files.add(file2);
        ArrayList<File> files1 = new ArrayList<>();
        files1.add(file3);
        taskSends.add(new TaskSend(1, files));
        taskSends.add(new TaskSend(2, files1));
        taskSends.add(new TaskSend(3, files1));
        taskSends.add(new TaskSend(4, files1));
        taskSends.add(new TaskSend(5, files1));
        taskSends.add(new TaskSend(6, files1));

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
                    System.out.println("Speed: " + String.format("%.2f",((((double)total_sent) - prev_sent)/((current - prev_time)/1000.0))/(1024.0/Partitioner.partitionSize)) + " KB/s");
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

    public static void main(String[] args) throws UnknownHostException {
        Sender sender = new Sender();
        sender.sendFileTest();




    }
}
