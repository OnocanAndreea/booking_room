package com.example.booking_room.person;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class RegisterPersonRequest {

    @NonNull
    String firstName;
    @NonNull
    String lastName;
    @NonNull
    String phoneNumber;
    @NonNull
    String email;
    @NonNull
    String role;
}
