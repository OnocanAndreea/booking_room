package com.example.booking_room.reservation;


import com.example.booking_room.reservation.controller.data.JsonReservationResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.Date;

@Value
@Builder
public class UpdateReservationRequest {

    @JsonProperty("reservationID")
    Integer reservationID;

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

}
