package com.example.booking_room.reservation.repository;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Builder
@Setter
@Getter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "reservation")

public class ReservationEntity {

    @Id
    @GeneratedValue

    @Column(name = "reservation_id")
    public Integer reservationID;

    @Column(name = "number_of_invited_persons")
    public Integer numberOfInvitedPersons;

    @Column(name = "arrival_date")
    public LocalDate arrivalDate;

    @Column(name = "departure_date")
    public LocalDate departureDate;

    @Column(name = "reserved_room_id")
    public Integer reservedRoomID;

    @Column(name = "organizer_person_id")
    public Integer organizerPersonID;

}
