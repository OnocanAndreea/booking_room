package com.example.booking_room.person;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

@Builder
@Value
public class  Person {
    //@NonNull // this way it gives an error: personID is marked non-null but is null
    @Nullable   // I tried with Nullable, but it gives another error
            Integer personID;
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
