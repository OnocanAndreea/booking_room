package com.example.booking_room.room.service;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

@Builder
@Value
public class RegisterRoomRequest {

    @Nullable
    Integer roomID;
    @NonNull
    Integer numberOfSits;
    @NonNull
    Integer roomAddressID;
    @NonNull
    String type;
}

