package com.example.bookshop_app.dto.form;

public class UserProfileForm {
    private String name;
    private String mail;
    private String phone;
    private String password;
    private String passwordReply;

    public UserProfileForm() {
    }

    public UserProfileForm(String name, String mail, String phone, String password, String passwordReply) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.password = password;
        this.passwordReply = passwordReply;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordReply() {
        return passwordReply;
    }

    public void setPasswordReply(String passwordReply) {
        this.passwordReply = passwordReply;
    }
}
