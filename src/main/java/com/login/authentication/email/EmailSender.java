package com.login.authentication.email;

public interface EmailSender {
    void send(String to, String email);
}
