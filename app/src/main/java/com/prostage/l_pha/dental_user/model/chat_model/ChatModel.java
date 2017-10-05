package com.prostage.l_pha.dental_user.model.chat_model;

import java.io.Serializable;

/**
 * Created by USER on 31-Mar-17.
 */

public class ChatModel implements Serializable{

    private String senderId;
    private String date;
    private String text;
    private Boolean read;

    private FileModel file;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    public ChatModel() {
    }

    public ChatModel(String senderId, String date, String text, Boolean read, FileModel file) {
        this.senderId = senderId;
        this.date = date;
        this.text = text;
        this.read = read;
        this.file = file;
    }
}
