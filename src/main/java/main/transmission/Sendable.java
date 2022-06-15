package main.transmission;

import java.util.Map;

public interface Sendable {
    void BuildMessage(Map<String, String> message);
    void send(SocketClient socketClient);
}
