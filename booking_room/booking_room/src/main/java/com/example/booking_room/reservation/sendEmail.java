package com.example.booking_room.reservation;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;

public class sendEmail {

    private static final String API_KEY = "pubkey-3de49f5cc17e1686a44540b1abc2be4a";
    private static final String YOUR_DOMAIN_NAME = "sandboxecdbda38748f42408e2bcbc89c80b3a0.mailgun.org";

    public MessageResponse sendSimpleMessage() {
        //todo how to send emails from java using mailgun
        MailgunMessagesApi mailgunMessagesApi = MailgunClient.config(API_KEY)
                .createApi(MailgunMessagesApi.class);

        Message message = Message.builder()
                .from("Excited User <USER@YOURDOMAIN.COM>")
                .to("artemis@example.com")
                .subject("Hello")
                .text("Testing out some Mailgun awesomeness!")
                .build();

        return mailgunMessagesApi.sendMessage(YOUR_DOMAIN_NAME, message);
    }

}
