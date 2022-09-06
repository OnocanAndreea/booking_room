package com.example.booking_room.room.repository;

import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class RoomEntity {

    @Id
    @GeneratedValue

    @Column(name = "room_id")
    public Integer roomID;

    @Column(name = "number_of_seats")
    public Integer numberOfSeats;

    @Column(name = "room_address_id")
    public Integer roomAddressID;

    @Column(name = "type")
    public String type;
}
