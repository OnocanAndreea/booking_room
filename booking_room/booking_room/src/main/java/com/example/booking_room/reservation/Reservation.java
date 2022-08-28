package com.example.booking_room.reservation;


import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.Date;

@Value
@Builder
public class Reservation {

    @Nullable
    Integer reservationID;

    @NonNull
    Integer numberOfInvitedPersons;

    @NonNull
    LocalDate arrival_date;

    @NonNull
    LocalDate departure_date;

    @NonNull
    Integer reservedRoomID;

    @NonNull
    Integer organizerPersonID;



}
