package main.partition;

import main.message.Message;
import main.message.MessageDATA;

public interface PartitionListener {
    void onPartitionBlock(MessageDATA message);
}
