package main.application;

import main.transmission.SocketClient;

import java.util.HashMap;

public interface ITask {
    void start(SocketClient socketClient);
    void resume(Boolean paused);
    void pause(Boolean paused);
    HashMap<Integer, Integer> getProgress();
    Boolean isPaused();
}
