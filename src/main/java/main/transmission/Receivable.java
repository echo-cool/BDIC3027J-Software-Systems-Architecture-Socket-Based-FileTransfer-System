package main.transmission;

import main.message.MessageACK;

import java.util.Map;

public interface Receivable {
    Map<String, String> parse();
    MessageACK receive();
}
