package com.example.bookshop_app.dto;

public class PayDto {
    private String url;

    public PayDto(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
