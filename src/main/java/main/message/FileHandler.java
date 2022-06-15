package main.message;

import main.transmission.IMessageHandler;
import main.file.AbstractPartitioner;
import main.transmission.TaskReceive;
import main.transmission.ListeningThread;

import java.util.HashMap;

public class FileHandler implements IMessageHandler {
    public static HashMap<Integer, TaskReceive> taskHashMap = new HashMap<>();
    @Override
    public void onReceive(ListeningThread.Connection connection, String message) {
//        System.out.println(message);
        if(Message.getMessageType(message) == Message.Type.SYN) {
            MessageSYN synMessage = new MessageSYN(message);
//            System.out.println("SYN message received");
//            System.out.println("SYN message: " + synMessage);
            synchronized (taskHashMap) {
                if (taskHashMap.containsKey(Integer.parseInt(synMessage.getTaskID()))) {
//                    System.out.println("Task already exists, adding to taskHashMap");
                    taskHashMap.get(Integer.parseInt(synMessage.getTaskID())).addFile(new AbstractPartitioner.SegmentedData(
                            Integer.parseInt(synMessage.getTaskID()),
                            Integer.parseInt(synMessage.getFileID()),
                            synMessage.getFileName(),
                            synMessage.getSegmentCount()
                    ));

                } else {
//                    System.out.println("Task does not exist, creating new task");
                    TaskReceive taskReceive = new TaskReceive(Integer.parseInt(synMessage.getTaskID()));
                    taskReceive.addFile(new AbstractPartitioner.SegmentedData(
                            Integer.parseInt(synMessage.getTaskID()),
                            Integer.parseInt(synMessage.getFileID()),
                            synMessage.getFileName(),
                            synMessage.getSegmentCount()
                    ));
                    taskHashMap.put(Integer.parseInt(synMessage.getTaskID()), taskReceive);
                }
            }
            MessageACK ackMessage = synMessage.receive();
            connection.println(ackMessage.getMessage());

        }else if (Message.getMessageType(message) == Message.Type.DATA) {
//            System.out.println("DATA message received");
            MessageDATA dataMessage = new MessageDATA(message);
            int taskID = Integer.parseInt(dataMessage.getTaskID());
            int fileID = Integer.parseInt(dataMessage.getFileID());
            int segmentID = dataMessage.getSegmentIndex();
            String data = dataMessage.getSegmentData();
            if (taskHashMap.containsKey(taskID)) {
                TaskReceive taskReceive = taskHashMap.get(taskID);
                taskReceive.updateSegmentedData(fileID, segmentID, data);
                MessageACK ackMessage = dataMessage.receive();
                taskReceive.sendACK(ackMessage, connection);

//                SegmentedData segmentedData = receiveTask.getSegmentedData(fileID);
//                segmentedData.addMessage(segmentID, data);
//                System.out.println(dataMessage);
//                ACK_Message ackMessage = new ACK_Message(taskID, fileID, segmentID);
//                connection.println(ackMessage.getMessage());
//                if (segmentedData.isFinished()) {
//                    System.out.println("File received, saving...");
//                    Receiver.WriteToFile(segmentedData.getFileName(), segmentedData.assemble());
//                }

            }else{
                System.out.println("Task not found");
            }
        }
    }
}