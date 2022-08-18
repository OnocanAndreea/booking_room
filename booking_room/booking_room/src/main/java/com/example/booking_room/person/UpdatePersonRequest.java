package com.example.booking_room.person;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
@Builder
public class UpdatePersonRequest {
   /* @NonNull
    Integer personID; //todo drop, id sent as path variable*/
    @Nullable
    String firstName;
    @Nullable
    String lastName;
    @Nullable
    String phoneNumber;
    @Nullable
    String email;
    @Nullable
    String role;
}
