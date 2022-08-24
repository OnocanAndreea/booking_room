package com.example.booking_room.address.controller.data;


import com.example.booking_room.address.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
@JsonDeserialize(builder = JsonAddressResponse.JsonAddressResponseBuilder.class)
public class JsonAddressResponse {

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


    @NonNull
    public static JsonAddressResponse toJson(final Address address){
        return JsonAddressResponse.builder()
                .addressID(address.getAddressID())
                .city(address.getCity())
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .floor(address.getFloor())
                .roomNumber(address.getRoomNumber())
                .build();
    }
}
