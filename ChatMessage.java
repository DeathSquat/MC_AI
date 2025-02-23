package com.eyantra.mind_cure_ai;

public class ChatMessage {
    private final String message;
    private final boolean isBotMessage; // True for bot, False for user

    public ChatMessage(String message, boolean isBotMessage) {
        this.message = message;
        this.isBotMessage = isBotMessage;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBot() {
        return isBotMessage;
    }
}
