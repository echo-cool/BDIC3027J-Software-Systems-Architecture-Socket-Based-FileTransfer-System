package main.transmission;

import main.file.AbstractPartitioner;
import main.application.Receiver;
import main.message.MessageACK;

import java.util.HashMap;

public class TaskReceive {
    private int taskID;
    private HashMap<Integer, AbstractPartitioner.SegmentedData> files = new HashMap<>();
    private boolean isPaused = false;

    public TaskReceive(int taskID) {
        this.taskID = taskID;
    }

    public void addFile(AbstractPartitioner.SegmentedData file) {
        files.put(file.getFileID(), file);
    }

    public AbstractPartitioner.SegmentedData getSegmentedData(int fileID) {
        return files.get(fileID);
    }

    public void updateSegmentedData(int fileID, int segmentID, String data) {
//        System.out.println("Updating segment " + segmentID + " of file " + fileID);
        AbstractPartitioner.SegmentedData segmentedData = files.get(fileID);
        segmentedData.addMessage(segmentID, data);
        if (segmentedData.isFinished()) {
//            System.out.println("File received, saving...");
            Receiver.WriteToFile(segmentedData.getFileName(), segmentedData.assemble(), "Task-" + taskID + "_" + fileID + "_");
        }

    }

    public HashMap<Integer, AbstractPartitioner.SegmentedData> getFiles() {
        return files;
    }

    public int getTaskID() {
        return taskID;
    }

    public void sendACK(MessageACK ackMessage, ListeningThread.Connection connection) {
        while (isPaused) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        connection.println(ackMessage.getMessage());
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TaskReceive{" +
                "TaskID=" + taskID +
                ", numberOfFiles=" + files.size() +
                '}');
            sb.append("\t");
            sb.append("[Task Started]");
            if (this.isPaused){
                sb.append("\t");
                sb.append("[Paused]");
            }
        sb.append("\n");
        for(int i = 0; i < files.size(); i ++){
            AbstractPartitioner.SegmentedData file = files.get(i);
                Integer progress = file.getMessages().size();
                Integer totalSegment = file.getTotalSegments();
                sb.append("\tFile{" +
                        "name=" + file.getFileName() +
                        ", totalSegment=" + totalSegment +
                        ", progress=" + progress +
                        "}");
            sb.append("\n");
        }
        return sb.toString();
    }

}
