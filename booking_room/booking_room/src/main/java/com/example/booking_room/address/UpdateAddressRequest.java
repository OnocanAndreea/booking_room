package com.example.booking_room.address;

import com.example.booking_room.address.controller.data.JsonAddressResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.Valid;


@Value
@Builder

public class UpdateAddressRequest {
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
    private static JsonAddressResponse fromEntity(@NonNull final Address address) {
        return JsonAddressResponse.builder()
                .addressID(address.getAddressID())
                .city(address.getCity())
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .floor(address.getFloor())
                .roomNumber(address.getRoomNumber())
                .build();
    }

    @NonNull
    private static Address toEntity(@NonNull final JsonAddressResponse jsonAddressResponse){
        return Address.builder()
                .addressID(jsonAddressResponse.getAddressID())
                .city(jsonAddressResponse.getCity())
                .street(jsonAddressResponse.getStreet())
                .streetNumber(jsonAddressResponse.getStreetNumber())
                .floor(jsonAddressResponse.getFloor())
                .roomNumber(jsonAddressResponse.getRoomNumber())
                .build();
    }
}
