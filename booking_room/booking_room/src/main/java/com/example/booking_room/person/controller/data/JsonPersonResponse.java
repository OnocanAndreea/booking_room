package com.example.booking_room.person.controller.data;

import com.example.booking_room.person.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = JsonPersonResponse.JsonPersonResponseBuilder.class)

public class JsonPersonResponse {

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
    public static JsonPersonResponse toJson(final Person person) {
        return JsonPersonResponse.builder()
                .personID(person.getPersonID())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .role(person.getRole())
                .email(person.getEmail())
                .phoneNumber(person.getPhoneNumber())
                .build();
    }

}
