package com.example.booking_room.person;

import com.example.booking_room.person.controller.data.JsonPersonResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

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

 @NonNull
 private static JsonPersonResponse fromEntity(@NonNull final Person person) {
  return JsonPersonResponse.builder()
          .personID(person.getPersonID())
          .firstName(person.getFirstName())
          .lastName(person.getLastName())
          .role(person.getRole())
          .email(person.getEmail())
          .phoneNumber(person.getPhoneNumber())
          .build();
 }

 @NonNull
 private static Person fromEntity(@NonNull final JsonPersonResponse jsonPersonResponse) {
  return Person.builder()
          .personID(jsonPersonResponse.getPersonID())
          .firstName(jsonPersonResponse.getFirstName())
          .lastName(jsonPersonResponse.getLastName())
          .role(jsonPersonResponse.getRole())
          .email(jsonPersonResponse.getEmail())
          .phoneNumber(jsonPersonResponse.getPhoneNumber())
          .build();
 }
}
