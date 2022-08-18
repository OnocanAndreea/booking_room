package com.example.booking_..data;

import com.example.booking_..service.;
import com.example.booking_..service.UpdateRequest;
import com.example.booking_..service.RegisterRequest;
import com.example.booking_..service.
import lombok.NonNull;
import org.apache.coyote.Response;
import org.hibernate.SessionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
//endpoint by default

@RequestMapping("room");

public class RoomController {

    private final SessionFactory hibernateFactory;

}
