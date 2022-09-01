package com.example.booking_room.reservation;

import com.example.booking_room.reservation.controller.data.JsonReservationResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.Date;


@Builder
@Value
public class RegisterReservationRequest {

    @JsonProperty("numberOfInvitedPersons")
    Integer numberOfInvitedPersons;

    @JsonProperty("arrivalDate")
    String arrivalDate;

    @JsonProperty("departureDate")
    String departureDate;

    @JsonProperty("reservedRoomID")
    Integer reservedRoomID;

    @JsonProperty("organizerPersonID")
    Integer organizerPersonID;

    @JsonProperty("personID")
    Integer personID;

    @JsonProperty("roomID")
    Integer roomID;


}
