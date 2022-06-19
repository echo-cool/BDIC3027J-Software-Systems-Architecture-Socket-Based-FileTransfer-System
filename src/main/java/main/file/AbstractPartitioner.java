package main.file;

import main.message.MessageDATA;
import main.transmission.IPartitioner;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public abstract class AbstractPartitioner implements IPartitioner {
    public static final int partitionSize = (int) (1024 * 5)/1024;
    int partitionCount = 0;

    public static SegmentedData partition(File file, int taskID, int FileID, PartitionListener listener){
        int total_size = 0;
        total_size = (int) file.length();
        int partitionCount = (int) Math.ceil(((double) total_size) / partitionSize);
        SegmentedData segmentedData = new SegmentedData(taskID, FileID, partitionCount);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            for(int i = 0; i < partitionCount; i++){
                byte[] buffer = new byte[partitionSize];
                int read = bis.read(buffer);
                String base64 = Base64.getEncoder().encodeToString(buffer);
//                String data = new String(buffer, 0, read);
                listener.onPartitionBlock(
                        new MessageDATA(
                                taskID,
                                FileID,
                                i,
                                base64
                        ));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return segmentedData;
    }

    public static interface PartitionListener {
        void onPartitionBlock(MessageDATA message);
    }

    public static class SegmentedData {
        private ArrayList<MessageDATA> messages;
        private int totalSegments;
        private int taskID;
        private int FileID;
        private String fileName;

        public SegmentedData(int taskID, int fileID, int totalSegments) {
            this.messages = new ArrayList<>();
            this.totalSegments = totalSegments;
            this.taskID = taskID;
            this.FileID = fileID;
        }
        public SegmentedData(int taskID, int fileID, String fileName, int totalSegments) {
            this.messages = new ArrayList<>();
            this.taskID = taskID;
            this.FileID = fileID;
            this.fileName = fileName;
            this.totalSegments = totalSegments;

        }

        public byte[] assemble() {
            ArrayList<byte[]> data = new ArrayList<>();
            for (MessageDATA message : messages) {
                byte[] tmp_data = Base64.getDecoder().decode(message.getSegmentData());
                data.add(tmp_data);
            }
            byte[] final_data = new byte[data.size() * data.get(0).length];
            int index = 0;
            for (byte[] tmp_data : data) {
    //            System.out.println(Arrays.toString(tmp_data));
                for (int i = 0; i < tmp_data.length; i++) {
                    final_data[index] = tmp_data[i];
                    index++;
                }
            }
            return final_data;
    //        StringBuilder sb = new StringBuilder();
    //        int assembled_count = 0;
    //        for(int i = 0; i < totalSegments; i++) {
    //            for (DATA_Message message : messages) {
    //                if (message.getSegmentIndex() == i) {
    //                    sb.append(message.getSegmentData());
    //                    assembled_count++;
    //                    break;
    //                }
    //            }
    //        }
    //        if (assembled_count != totalSegments) {
    //            throw new RuntimeException("Not all segments assembled");
    //        }
    //        return sb.toString();
        }

        public void addMessage(int index, String data) {
            messages.add(new MessageDATA(
                    taskID,
                    FileID,
                    index,
                    data
            ));
        }

        public void addMessages(ArrayList<MessageDATA> messages) {
            this.messages.addAll(messages);
        }

        public ArrayList<MessageDATA> getMessages() {
            return messages;
        }

        public int getTotalSegments() {
            return totalSegments;
        }

        @Override
        public String toString() {
            return "SegmentedData{" +
                    "messages=" + messages +
                    ", totalSegments=" + totalSegments +
                    ", taskID=" + taskID +
                    ", FileID=" + FileID +
                    '}';
        }

        public int getTaskID() {
            return taskID;
        }

        public int getFileID() {
            return FileID;
        }

        public Boolean isFinished() {
    //        System.out.println("Total segments: " + totalSegments);
    //        System.out.println("Messages size: " + messages.size());
            return messages.size() == totalSegments;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
