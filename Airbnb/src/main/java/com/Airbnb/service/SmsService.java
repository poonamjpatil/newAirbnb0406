package com.Airbnb.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import static java.lang.ref.Cleaner.create;

@Configuration
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;


    public void sendSMS(String toPhoneNumber, String messageContent) {
        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),      // Recipient
                new PhoneNumber(fromPhoneNumber), // Sender
                messageContent                      // Message body
        ).create();
        System.out.println("Message sent! SID: " + message.getSid());

    }
}



