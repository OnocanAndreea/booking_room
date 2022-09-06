package com.example.booking_room.reservation.controller;

import com.example.booking_room.reservation.CheckDate;
import com.example.booking_room.reservation.RegisterReservationRequest;
import com.example.booking_room.reservation.SendEmail;
import com.example.booking_room.reservation.UpdateReservationRequest;
import com.example.booking_room.reservation.controller.data.JsonGetReservationListResponse;
import com.example.booking_room.reservation.controller.data.JsonReservationResponse;
import com.example.booking_room.reservation.service.ReservationService;
import com.example.booking_room.room.controller.data.JsonGetRoomListResponse;


import kong.unirest.JsonNode;
import kong.unirest.UnirestException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RestController
//endpoint by default
@RequestMapping("reservation")
public class ReservationController {

    @NonNull
    private final ReservationService reservationService;
    private final SendEmail sendEmail;


    public ReservationController(@NonNull ReservationService reservationService, SendEmail sendEmail) {
        this.reservationService = reservationService;
        this.sendEmail = sendEmail;
    }

    @RequestMapping (value = "/test", method = RequestMethod.POST)
    public ResponseEntity<?> getTest() {
        try {

            JsonNode jsonNode = sendEmail.sendSimpleMessage();
            System.out.println(jsonNode);
            return ResponseEntity.ok(jsonNode);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping (value = "/accept")
    public ResponseEntity<?> getAccept() {
        try {

            return ResponseEntity.ok("you accept the invitation");
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping(value = "/accept/{reservationID}")
    public ResponseEntity<?> acceptByID(@PathVariable Integer reservationID) {
        try {
            JsonReservationResponse jsonReservationResponse = reservationService.acceptOrDeclineByID(reservationID,true);

            return ResponseEntity.ok(jsonReservationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
    @GetMapping  (value = "/decline")
    public ResponseEntity<?> getDecline() {
        try {

            return ResponseEntity.ok("you declined the invitation");
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping(value = "/decline/{reservationID}")
    public ResponseEntity<?> declineByID(@PathVariable Integer reservationID) {
        try {
            JsonReservationResponse jsonReservationResponse = reservationService.acceptOrDeclineByID(reservationID,false);
            return ResponseEntity.ok(jsonReservationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "/available_rooms")
    public ResponseEntity<?> getAllRoomsAvailable(@RequestBody @NonNull final CheckDate checkDate) {
        try {
            JsonGetRoomListResponse jsonAvailableRoomIds = reservationService.getAllRoomsAvailable(checkDate);
            return ResponseEntity.ok(jsonAvailableRoomIds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //works
    @GetMapping(value = "/reservations")
    public ResponseEntity<?> getAll() {
        System.out.println("request");
        try {
            JsonGetReservationListResponse jsonGetReservationListResponse = reservationService.getAllReservations();
            return ResponseEntity.ok(jsonGetReservationListResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }
    }

    //works, but there is no Exception handled
    @GetMapping(value = "/{reservationID}")
    public ResponseEntity<?> getById(@PathVariable Integer reservationID) {
        try {
            JsonReservationResponse jsonReservationResponse = reservationService.getReservation(reservationID);
            return ResponseEntity.ok(jsonReservationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // works, but there is no Exception handled
    @RequestMapping(value = "/{reservationID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer reservationID) {
        try {
            reservationService.deleteReservationByID(reservationID);
            return ResponseEntity.ok("the reservation was deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // works
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody RegisterReservationRequest registerReservationRequest) {

        try {
            JsonReservationResponse jsonReservationResponse = reservationService.registerReservation(
                    registerReservationRequest,
                    registerReservationRequest.getRoomID(),
                    registerReservationRequest.getPersonID());
            sendEmail.sendConfirmationDetails(registerReservationRequest);

            return ResponseEntity.ok(jsonReservationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }

    }
    @PutMapping(value = "/{reservationID}")
    public ResponseEntity<?> update(@RequestBody UpdateReservationRequest updateReservationRequest, @PathVariable Integer reservationID) {
        System.out.println("Reservation with personId:" + reservationID + " to update");
        try {
            JsonReservationResponse jsonReservationResponse = reservationService.updateReservation(reservationID, updateReservationRequest);
            return ResponseEntity.ok(jsonReservationResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

}
