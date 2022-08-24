package com.example.booking_room.address;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterAddressRequest {

    @JsonProperty("addressID")
    Integer addressID;

    @JsonProperty("city")
    String city;

    @JsonProperty("street")
    String street;

    @JsonProperty("streetNumber")
    Integer streetNumber;

    @JsonProperty("floor")
    Integer floor;

    @JsonProperty("roomNumber")
    Integer roomNumber;

}
