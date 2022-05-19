package main.message;

import java.util.Map;

public class MessageACK extends Message {
    public static final Type TYPE = Type.ACK;
    private String TaskID;
    private String FileID;
    private int SegmentIndex;


//    public ACK_Message(int taskID, int fileID, int segmentIndex) {
//        Map<String, String> data = new HashMap<>();
//        this.TaskID = String.valueOf(taskID);
//        this.FileID = String.valueOf(fileID);
//        this.SegmentIndex = segmentIndex;
//        data.put("TASK_ID", String.valueOf(TaskID));
//        data.put("FILE_ID", String.valueOf(FileID));
//        data.put("SEGMENT_INDEX", String.valueOf(SegmentIndex));
//        BuildMessage(data);
//    }

    public MessageACK(String message) {
        super(message);
        Map<String, String> data = this.parse();
        this.TaskID = data.get("TASK_ID");
        this.FileID = data.get("FILE_ID");
        try{
            // SYN message does not have segment index
            this.SegmentIndex = Integer.parseInt(data.get("SEGMENT_INDEX"));
        }
        catch (NumberFormatException e){
            this.SegmentIndex = -1;
        }
    }

    @Override
    public String toString() {
        return "ACK_Message{" +
                "TaskID='" + TaskID + '\'' +
                ", FileID='" + FileID + '\'' +
                ", SegmentIndex=" + SegmentIndex +
                '}';
    }

    @Override
    public Type getFlagType() {
        return TYPE;
    }

    public String getTaskID() {
        return TaskID;
    }

    public String getFileID() {
        return FileID;
    }

    public int getSegmentIndex() {
        return SegmentIndex;
    }
}
