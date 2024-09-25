package com.jgs.socketiochat;

public class Chat {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_JOIN = 1;
    public static final int TYPE_LEAVE = 2;

    private int id = 0;
    private String username;
    private String text;
    private boolean isSelf;
    private int type;

    public Chat() {

    }

    public Chat(String username, String text) {
        this.username = username;
        this.text = text;
    }

    public Chat(String username, String text, int type) {
        this.username = username;
        this.text = text;
        this.type = type;
    }

    public Chat(String username, String text, boolean isSelf, int type) {
        this.username = username;
        this.text = text;
        this.isSelf = isSelf;
        this.type = type;
    }

    public Chat(String username, int type) {
        this.username = username;
        this.type = type;
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

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

}
