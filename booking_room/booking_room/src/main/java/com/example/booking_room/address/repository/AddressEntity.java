package com.example.booking_room.address.repository;


import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue

    @Column(name = "address_id")
    public Integer addressID;

    @Column(name = "city")
    public String city;

    @Column(name = "street")
    public String street;

    @Column(name = "street_number")
    public Integer streetNumber;

    @Column(name = "floor")
    public Integer floor;

    @Column(name = "room_number")
    public Integer roomNumber;

}
