package com.example.booking_room.reservation;


import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Value
@Builder
public class Reservation {

    @Nullable
    Integer reservationID;

    @NonNull
    Integer numberOfInvitedPersons;

    @NonNull
    LocalDate arrivalDate;

    @NonNull
    LocalDate departureDate;

    @NonNull
    Integer reservedRoomID;

    @NonNull
    Integer organizerPersonID;

    @Builder.Default
    Boolean accepted = false;



}
