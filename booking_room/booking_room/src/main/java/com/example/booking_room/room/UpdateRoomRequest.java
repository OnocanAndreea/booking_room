package com.example.booking_room.room;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

@Builder
@Value
public class UpdateRoomRequest {

    @Nullable
    Integer roomID;
    @NonNull
    Integer numberOfSeats;
    @NonNull
    Integer roomAddressID;
    @NonNull
    String type;

}
