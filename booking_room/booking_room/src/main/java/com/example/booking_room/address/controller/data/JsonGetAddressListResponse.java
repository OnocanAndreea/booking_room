package com.example.booking_room.address.controller.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;


@Value
@Builder
public class JsonGetAddressListResponse {

    @JsonProperty("addressList")
    List<JsonAddressResponse> addresses;
}
