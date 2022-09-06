package com.example.booking_room.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;


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
