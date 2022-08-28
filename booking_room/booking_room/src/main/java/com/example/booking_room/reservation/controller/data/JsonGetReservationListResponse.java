package com.example.booking_room.reservation.controller.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class JsonGetReservationListResponse {

    @JsonProperty("reservationList")
    List<JsonReservationResponse> reservations;
}
