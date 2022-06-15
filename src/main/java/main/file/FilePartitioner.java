package main.file;

import main.message.MessageDATA;

import java.io.*;
import java.util.Base64;

public class FilePartitioner extends AbstractPartitioner {
    public static int getSegmentCount(File file){
        int total_size = (int) file.length();
        int partitionCount = (int) Math.ceil(((double) total_size) / partitionSize);
        return partitionCount;
    }
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
}
