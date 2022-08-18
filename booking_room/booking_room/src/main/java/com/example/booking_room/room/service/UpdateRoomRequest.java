package com.example.booking_room.room.service;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

public class UpdateRoomRequest {
    @Nullable
    Integer roomID;
    @NonNull
    Integer numberOfSits;
    @NonNull
    Integer roomAddressID;
    @NonNull
    String type;
}
