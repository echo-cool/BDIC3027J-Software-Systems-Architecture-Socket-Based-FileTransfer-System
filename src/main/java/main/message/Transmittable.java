package main.message;

import main.socket.SocketClient;

import java.util.Map;

public interface Transmittable {
    void BuildMessage(Map<String, String> message);
    Map<String, String> parse();
    MessageACK getACK_Message();
    void send(SocketClient socketClient);


}
