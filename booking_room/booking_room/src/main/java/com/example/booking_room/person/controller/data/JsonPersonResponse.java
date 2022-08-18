package com.example.booking_.person.controller.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@JsonDeserialize(builder = JsonPersonResponse.JsonPersonResponseBuilder.class)
public class JsonPersonResponse {

    @JsonProperty("name")
    String name;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-mm-dd")
    LocalDate createdAt;
}
