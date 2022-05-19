package main.message;

import java.util.Base64;

public class MessageUtil {
    public static String base64Encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String base64Decode(String data) {
        return new String(Base64.getDecoder().decode(data));
    }
}
