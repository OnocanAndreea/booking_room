package com.example.booking_room.room.controller.data;


import com.example.booking_room.room.Room;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = JsonRoomResponse.JsonRoomResponseBuilder.class)

public class JsonRoomResponse {

    @JsonProperty("roomID")
    Integer roomID;

    @JsonProperty("numberOfSeats")
    Integer numberOfSeats;

    @JsonProperty("roomAddressID")
    Integer roomAddressID;

    @JsonProperty("type")
    String type;

    @NonNull
    public static JsonRoomResponse toJson(final Room room) {
        return JsonRoomResponse.builder()
                .roomID(room.getRoomID())
                .numberOfSeats(room.getNumberOfSeats())
                .roomAddressID(room.getRoomAddressID())
                .type(room.getType())
                .build();
    }
}
