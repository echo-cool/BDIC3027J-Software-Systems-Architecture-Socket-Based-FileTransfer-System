package main.task;

import main.message.MessageDATA;
import main.message.MessageSYN;
import main.partition.FilePartitioner;
import main.partition.PartitionListener;
import main.socket.SocketClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskSend {
    private int TaskID;
    private int numberOfFiles = 0;
    private ArrayList<File> files = new ArrayList<>();
    private Boolean isPaused = false;
    private Boolean isStarted = false;
    private Boolean isFinished = false;
    private HashMap<Integer, Integer> totalSegment = new HashMap<>();

    private HashMap<Integer, Integer> progress = new HashMap<>();

    public TaskSend(int taskID, ArrayList<File> files) {
        this.TaskID = taskID;
        this.numberOfFiles = files.size();
        this.files = files;

    }

    public TaskSend(int taskID) {
        this.TaskID = taskID;
        this.numberOfFiles = 0;

    }

    public void addFile(File file) {
        this.files.add(file);
        this.numberOfFiles++;
    }

    public void start(SocketClient socketClient) {
        isStarted = true;
        for (int i = 0; i < numberOfFiles; i++) {
            MessageSYN _messageSYN = new MessageSYN(TaskID, i, files.get(i).getName(), FilePartitioner.getSegmentCount(files.get(i)));
            _messageSYN.send(socketClient);
            totalSegment.put(i, FilePartitioner.getSegmentCount(files.get(i)));
        }
        for (int i = 0; i < numberOfFiles; i++) {
            int finalI = i;
            FilePartitioner.partition(files.get(i), TaskID, i, new PartitionListener() {
                @Override
                public void onPartitionBlock(MessageDATA message) {
                    while (isPaused) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    message.send(socketClient);
                    progress.put(Integer.parseInt(message.getFileID()), message.getSegmentIndex() + 1);
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }
        this.isFinished = true;
    }

    public int getTaskID() {
        return TaskID;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public Boolean getPaused() {
        return isPaused;
    }

    public HashMap<Integer, Integer> getProgress() {
        return progress;
    }

    public HashMap<Integer, Integer> getTotalSegment() {
        return totalSegment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TaskSend{" +
                "TaskID=" + TaskID +
                ", numberOfFiles=" + numberOfFiles +
                '}');
        if (this.isStarted){
            sb.append("\t");
            sb.append("[Task Started]");
            if (this.isPaused){
                sb.append("\t");
                sb.append("[Paused]");
            }
            else {
                sb.append("\t");
                if (this.isFinished){
                    sb.append("[Finished]");
                }
                else {
                    sb.append("[Running (Sending)]");
                }
            }
        }
        else{
            sb.append("\t");
            sb.append("[Not started]");
        }
        sb.append("\n");
        for(int i = 0; i < files.size(); i ++){
            File file = files.get(i);
            if(isStarted) {
                Integer progress = getProgress().get(i);
                Integer totalSegment = getTotalSegment().get(i);
                sb.append("\tFile{" +
                        "name=" + file.getName() +
                        ", totalSegment=" + totalSegment +
                        ", progress=" + progress +
                        "}");
            }
            else{
                sb.append("\tFile{" +
                        "name=" + file.getName() +
                        "}");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public void setPaused(Boolean paused) {
        isPaused = paused;
    }
}
