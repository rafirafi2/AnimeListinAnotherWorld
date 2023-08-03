package com.example.animelistinanotherworld.resetPassword;
import org.springframework.mail.SimpleMailMessage;

public interface email {

    public void sendEmail(SimpleMailMessage email);

}
