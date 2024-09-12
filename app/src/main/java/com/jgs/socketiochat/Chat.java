package com.jgs.socketiochat;

public class Chat {

    private int id = 0;
    private String username;
    private String text;
    private boolean isSelf;

    public Chat() {

    }

    public Chat(String userName, String text) {
        this.username = userName;
        this.text = text;
    }

    public Chat(String username, String text, boolean isSelf) {
        this.username = username;
        this.text = text;
        this.isSelf = isSelf;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelf() {
        return isSelf;
    }
    public void setSelf(boolean self) {
        isSelf = self;
    }
}
