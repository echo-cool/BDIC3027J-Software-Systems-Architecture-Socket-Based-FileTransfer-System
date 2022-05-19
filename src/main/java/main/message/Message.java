package main.message;

import main.socket.SocketClient;

import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Transmittable{
//    public static final String DATA_FLAG = "DATA";
//    public static final String ACK_FLAG = "ACK";
//    public static final String SYN_FLAG = "SYN";
    public static final String START_FLAG = "<START>";
    public static final String END_FLAG = "<END>";
    public static final String SEP_FLAG = "<SEP>";
    public static final String VALUE_FLAG = "->";
    private static final int MAX_TIMEOUT = 5000;
    private String message;

    public enum Type {
        SYN,
        DATA,
        ACK
    }


    public Message() {
        this.message = "";
    }

    public Message(String message) {
        this.message = message;
    }

    public void BuildMessage(Map<String, String> message) {
        this.message = START_FLAG + SEP_FLAG;
        this.message += "TYPE" + VALUE_FLAG + getFlagType() + SEP_FLAG;
        for (Map.Entry<String, String> entry : message.entrySet()) {
            this.message += entry.getKey() + VALUE_FLAG + entry.getValue() + SEP_FLAG;
        }
        this.message += END_FLAG;
    }

    public Map<String, String> parse() {
        return parse(this.message);
    }
    public static Map<String, String> parse(String message_string) {
        if(message_string.contains(START_FLAG) && message_string.contains(END_FLAG)) {
            String tmp_message =message_string.replace(START_FLAG, "").replace(END_FLAG, "");
            Map<String, String> message = new HashMap<>();
            String[] tokens = tmp_message.split(SEP_FLAG);
            for (int i = 1; i < tokens.length; i++) {
                String[] keyValue = tokens[i].split(VALUE_FLAG);
                message.put(keyValue[0], keyValue[1]);
            }
            return message;
        }
        else {
            throw new IllegalArgumentException("Message is not valid: " + message_string);
        }
    }

    public String getMessage() {
        return message;
    }

    public static Type getMessageType(String message) {
        String messageType = parse(message).get("TYPE");
        if (messageType == null) {
            throw new IllegalArgumentException("Message is not valid");
        }
        if (messageType.equals(Type.SYN.toString())) {
            return Type.SYN;
        }
        else if (messageType.equals(Type.DATA.toString())) {
            return Type.DATA;
        }
        return null;
    }

    public abstract Type getFlagType();

    public MessageACK getACK_Message() {
        return new MessageACK(getMessage());
    }

    public void send(SocketClient socketClient) {
        while (true) {
            socketClient.println(this.message);
            int timeout = 0;
            while (timeout < MAX_TIMEOUT) {
                if(socketClient.hasNextLine()){
                    if (this.getFlagType() == Type.SYN) {
//                        System.out.println("SYN message sent, waiting for ACK");
                        String message = socketClient.readLine();
                        MessageACK _messageACK = new MessageACK(message);
                        if (_messageACK.getFlagType() == Type.ACK) {
//                            System.out.println("ACK message: " + _messageACK.toString());
//                            System.out.println("ACK received, proceeding..");
                            return;
                        }
                    }
                    if (this.getFlagType() == Type.DATA) {
//                        System.out.println("DATA message sent, waiting for ACK");
                        String message = socketClient.readLine();
                        MessageACK _messageACK = new MessageACK(message);
                        if (_messageACK.getFlagType() == Type.ACK) {
//                            System.out.println("ACK message: " + _messageACK.toString());
//                            System.out.println("ACK received, proceeding..");
                            return;
                        }
                    }
                }
                timeout++;
//                System.out.println("No ACK received, retrying..");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println(this.message);
    }

}
