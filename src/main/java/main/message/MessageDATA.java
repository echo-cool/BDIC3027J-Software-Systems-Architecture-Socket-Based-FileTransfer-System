package main.message;

import java.util.HashMap;
import java.util.Map;

public class MessageDATA extends Message implements Comparable{
    public static final Type TYPE = Type.DATA;
    private String TaskID;
    private String FileID;
    private int SegmentIndex;
    private String SegmentData;


    public MessageDATA(int TaskID, int FileID , int SegmentIndex, String SegmentData) {
        Map<String, String> data = new HashMap<>();
        this.TaskID = String.valueOf(TaskID);
        this.FileID = String.valueOf(FileID);
        this.SegmentIndex = SegmentIndex;
        this.SegmentData = SegmentData;
        data.put("TASK_ID", String.valueOf(TaskID));
        data.put("FILE_ID", String.valueOf(FileID));
        data.put("SEGMENT_INDEX", String.valueOf(SegmentIndex));
        String encodedSegmentData = MessageUtil.base64Encode(SegmentData);
        data.put("SEGMENT_DATA", encodedSegmentData);
        BuildMessage(data);
    }

    public MessageDATA(String message) {
        super(message);
        Map<String, String> data = this.parse();
        this.TaskID = data.get("TASK_ID");
        this.FileID = data.get("FILE_ID");
        this.SegmentIndex = Integer.parseInt(data.get("SEGMENT_INDEX"));
        String decodedSegmentData = MessageUtil.base64Decode(data.get("SEGMENT_DATA"));
        this.SegmentData = decodedSegmentData;
    }



    @Override
    public Type getFlagType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "DATA_Message{" +
                "TaskID='" + TaskID + '\'' +
                ", FileID='" + FileID + '\'' +
                ", SegmentIndex=" + SegmentIndex +
                ", SegmentData='" + SegmentData + '\'' +
                '}';
    }

    public String getSegmentData() {
        return SegmentData;
    }

    public int getSegmentIndex() {
        return SegmentIndex;
    }

    @Override
    public int compareTo(Object o) {
        return SegmentIndex - ((MessageDATA) o).SegmentIndex;
    }

    public String getTaskID() {
        return TaskID;
    }

    public String getFileID() {
        return FileID;
    }
}
