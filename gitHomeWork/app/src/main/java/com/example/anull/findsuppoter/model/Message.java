package com.example.anull.findsuppoter.model;

/**
 * Created by vuanhlevis on 23/12/2017.
 */

public class Message {
    String content, username;

    public Message() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Message(String content, String username) {
        this.content = content;
        this.username = username;

    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
