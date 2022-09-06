package com.example.booking_room.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdatePersonRequest {
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
