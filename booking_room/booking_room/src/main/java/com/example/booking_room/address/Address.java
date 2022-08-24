package com.example.booking_room.address;


import org.springframework.lang.Nullable;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Address {

    @Nullable
    Integer addressID;
    @NonNull
    String city;
    @NonNull
    String street;
    @NonNull
    Integer streetNumber;
    @NonNull
    Integer floor;
    @NonNull
    Integer roomNumber;


}
