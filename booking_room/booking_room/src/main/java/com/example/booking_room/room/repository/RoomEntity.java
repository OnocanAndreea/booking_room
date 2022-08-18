package com.example.booking_room.room.repository;

import lombok.*;
import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Room")
public class RoomEntity{

        @Id
        //aici setam pk din db
        @GeneratedValue

        @Column(name = "room_id")
        public Integer roomID;

        @Column(name = "number_of_sits")
        public Integer numberOfSits;

        @Column(name = "room_address_id")
        public Integer roomAddressID;

        @Column(name = "type")
        public String type;
}
