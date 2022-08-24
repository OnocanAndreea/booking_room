package com.example.booking_room.person.controller.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder

public class JsonGetPersonListResponse {


        @JsonProperty("personList")
        List <JsonPersonResponse> persons;

}
