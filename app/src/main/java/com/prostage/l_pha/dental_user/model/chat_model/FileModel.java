package com.prostage.l_pha.dental_user.model.chat_model;

/**
 * Created by USER on 31-Mar-17.
 */

public class FileModel {
    private String url;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileModel() {
    }

    public FileModel(String url, String name) {
        this.url = url;
        this.name = name;
    }
}
