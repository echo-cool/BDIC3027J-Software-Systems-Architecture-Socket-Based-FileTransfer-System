package main.partition;

public class Partitioner {
    public static final int partitionSize = (int) (1024 * 5)/1024;
    int partitionCount = 0;

    public static SegmentedData partition(String data, int taskID, int FileID){
        int partitionCount = (int) Math.ceil(((double) data.length()) / partitionSize);
        SegmentedData segmentedData = new SegmentedData(taskID, FileID, partitionCount);
        for(int i = 0; i < partitionCount; i++){
            segmentedData.addMessage(
                    i,
                    data.substring(i * partitionSize, Math.min((i + 1) * partitionSize, data.length()))
            );
        }


        return segmentedData;
    }
}
