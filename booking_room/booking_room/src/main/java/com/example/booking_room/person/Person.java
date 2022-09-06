package com.example.booking_room.person;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

@Builder
@Value
public class  Person {

    @Nullable
    Integer personID;
    @NonNull
    String firstName;
    @NonNull
    String lastName;
    @NonNull
    Integer phoneNumber;
    @NonNull
    String email;
    @NonNull
    String role;
}
