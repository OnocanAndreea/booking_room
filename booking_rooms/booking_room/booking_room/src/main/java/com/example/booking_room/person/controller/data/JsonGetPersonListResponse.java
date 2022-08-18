package com.example.booking_.person.controller.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class JsonGetPersonListResponse {

    public class JsonGetPersonResponse {
        @JsonProperty("personList")
        List <JsonGetPersonResponse> persons;
    }
}
