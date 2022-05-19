package main.task;

import main.Receiver;
import main.message.MessageACK;
import main.partition.SegmentedData;
import main.socket.connection.Connection;

import java.util.HashMap;

public class TaskReceive {
    private int taskID;
    private HashMap<Integer, SegmentedData> files = new HashMap<>();
    private boolean isPaused = false;

    public TaskReceive(int taskID) {
        this.taskID = taskID;
    }

    public void addFile(SegmentedData file) {
        files.put(file.getFileID(), file);
    }

    public SegmentedData getSegmentedData(int fileID) {
        return files.get(fileID);
    }

    public void updateSegmentedData(int fileID, int segmentID, String data) {
        System.out.println("Updating segment " + segmentID + " of file " + fileID);
        SegmentedData segmentedData = files.get(fileID);
        segmentedData.addMessage(segmentID, data);
        if (segmentedData.isFinished()) {
            System.out.println("File received, saving...");
            Receiver.WriteToFile(segmentedData.getFileName(), segmentedData.assemble(), "Task-" + taskID + "_" + fileID + "_");
        }

    }

    public void sendACK(MessageACK ackMessage, Connection connection) {
        while (isPaused) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        connection.println(ackMessage.getMessage());
    }

}
