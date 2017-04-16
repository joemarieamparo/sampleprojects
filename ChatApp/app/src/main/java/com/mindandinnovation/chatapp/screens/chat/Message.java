package com.mindandinnovation.chatapp.screens.chat;

/**
 * Created by Lenovo ideapad on 4/15/2017.
 */
public class Message {
    private String message;
    private String user;

    public Message() {
    }

    public Message(String messageText, String messageUser) {
        this.message = messageText;
        this.user = messageUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
