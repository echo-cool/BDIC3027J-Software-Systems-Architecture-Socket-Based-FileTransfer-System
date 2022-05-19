package main.message;

import java.util.HashMap;
import java.util.Map;

public class MessageSYN extends Message {
    public static final Type TYPE = Type.SYN;
    private String TaskID;
    private String FileID;
    private String FileName;
    private int SegmentCount;

    public MessageSYN(int TaskID, int FileID , String FileName, int SegmentCount) {
        Map<String, String> data = new HashMap<>();
        this.TaskID = String.valueOf(TaskID);
        this.FileID = String.valueOf(FileID);
        this.FileName = FileName;
        this.SegmentCount = SegmentCount;
        data.put("TASK_ID", this.TaskID);
        data.put("FILE_ID", this.FileID);
        data.put("FILENAME", FileName);
        data.put("SEGMENT_COUNT", String.valueOf(SegmentCount));
        BuildMessage(data);
    }

    public MessageSYN(String message) {
        super(message);
        Map<String, String> data = this.parse();
        this.TaskID = data.get("TASK_ID");
        this.FileID = data.get("FILE_ID");
        this.FileName = data.get("FILENAME");
        this.SegmentCount = Integer.parseInt(data.get("SEGMENT_COUNT"));
    }

    @Override
    public Type getFlagType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "SYN_Message{" +
                "TaskID='" + TaskID + '\'' +
                ", FileID='" + FileID + '\'' +
                ", FileName='" + FileName + '\'' +
                '}';
    }

    public String getTaskID() {
        return TaskID;
    }

    public String getFileID() {
        return FileID;
    }

    public String getFileName() {
        return FileName;
    }

    public int getSegmentCount() {
        return SegmentCount;
    }
}
