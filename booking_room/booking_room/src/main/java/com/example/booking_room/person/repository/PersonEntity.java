package com.example.booking_room.person.repository;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Person")
public class PersonEntity {
    @Id
    //iti genereaza automat pk din db (in cazul nostru)
    @GeneratedValue
    @Column(name = "person_id")
    public Integer personID;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Column(name = "email")
    public String email;

    @Column(name = "role")
    public String role;
}
