package com.example.booking_room.reservation.controller.data;


import com.example.booking_room.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.Date;

@Value
@Builder
@JsonDeserialize(builder = JsonReservationResponse.JsonReservationResponseBuilder.class)
public class JsonReservationResponse {

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

    @NonNull
    public static JsonReservationResponse toJson(@NonNull final Reservation reservation){
        return JsonReservationResponse.builder()
                .reservationID(reservation.getReservationID())
                .numberOfInvitedPersons(reservation.getNumberOfInvitedPersons())
                .arrivalDate(reservation.getArrivalDate().toString())//am facut conversie in string deoarece postmanu nu suporta localdate-u
                .departureDate(reservation.getDepartureDate().toString())
                .reservedRoomID(reservation.getReservedRoomID())
                .organizerPersonID(reservation.getOrganizerPersonID())
                .build();
    }
}
