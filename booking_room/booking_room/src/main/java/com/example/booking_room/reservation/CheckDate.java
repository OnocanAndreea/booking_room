package com.example.booking_room.reservation;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;


@Value
@Builder
public class CheckDate {

    @NonNull
    String fromDate;
    @NonNull
    String toDate;
}
