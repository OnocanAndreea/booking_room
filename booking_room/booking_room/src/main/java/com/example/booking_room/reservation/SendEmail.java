package com.example.booking_room.reservation;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class SendEmail {
    private static final String API_KEY = "b92820011dddc90e12f9d381eba47405-07e2c238-23a8a892";
    private static final String YOUR_DOMAIN_NAME = "sandboxecdbda38748f42408e2bcbc89c80b3a0.mailgun.org";

    public static JsonNode sendSimpleMessage() throws UnirestException {

        HttpResponse<JsonNode> request =Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
			    .basicAuth("api", API_KEY)
                .queryString("from", "test@test.com")
                .queryString("to", "andreea.onocan14@gmail.com")
                .queryString("subject", "Hello from Andreea ONO,")
                .queryString("text", "I invite you to participate in this meeting that will take place on 08-08-2022 at 17:00 AM to discuss about: " +
                        "How schools can nurture every student's genius." + "The address of the location is: City: Cluj Street: Decebal Number: 102 Floor: 1."+
                        "Please let us know if you can make it !"+" http://localhost:8080/reservation/accept   "+
                        "http://localhost:8080/reservation/decline")
                .asJson();

        return request.getBody();
    }
    public static JsonNode sendConfirmationDetails(@NonNull final RegisterReservationRequest registerReservationRequest) throws UnirestException {

        HttpResponse<JsonNode> request =Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
                .basicAuth("api", API_KEY)
                .queryString("from", "test@test.com")
                .queryString("to", "andreea.onocan14@gmail.com")
                .queryString("subject", "Hello from Booking_rooms,")
                .queryString("text", "here are the details about your reservation")
                .queryString("text", MessageFormat.format("You have  successfully reserved roomNumber:{0} fromDate: {1}, toDate{2}",registerReservationRequest.getReservedRoomID(),registerReservationRequest.getArrivalDate(),registerReservationRequest.getDepartureDate()))
                .asJson();

        return request.getBody();
    }
}
