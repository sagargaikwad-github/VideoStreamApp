package com.example.videostream.ModelData;

public class urlModel {
    String username;
    String url;

    public urlModel(String username, String url) {
        this.username = username;
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
