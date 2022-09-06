package com.example.booking_room.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RegisterPersonRequest {

    @JsonProperty("personID")
    Integer personID;

    @JsonProperty("firstName")
    String firstName;

    @JsonProperty("lastName")
    String lastName;

    @JsonProperty("phoneNumber")
    Integer phoneNumber;

    @JsonProperty("email")
    String email;

    @JsonProperty("role")
    String role;

}
