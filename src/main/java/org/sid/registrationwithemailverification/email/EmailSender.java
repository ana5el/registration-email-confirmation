package org.sid.registrationwithemailverification.email;

public interface EmailSender {
    void send(String to, String email);
}
