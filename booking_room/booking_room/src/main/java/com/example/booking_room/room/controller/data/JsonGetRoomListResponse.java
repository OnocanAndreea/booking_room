package com.example.booking_room.room.controller.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder

public class JsonGetRoomListResponse {

    @JsonProperty("roomList")
    List<JsonRoomResponse> rooms;
}
