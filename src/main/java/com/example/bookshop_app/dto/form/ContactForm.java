package com.example.bookshop_app.dto.form;

public class ContactForm {
    private String name;
    private String mail;
    private String topic;
    private String message;

    public ContactForm() {
    }

    public ContactForm(String name, String mail, String topic, String message) {
        this.name = name;
        this.mail = mail;
        this.topic = topic;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
