package com.example.booking_room.reservation.controller;

import com.example.booking_room.reservation.CheckDate;
import com.example.booking_room.reservation.RegisterReservationRequest;
import com.example.booking_room.reservation.UpdateReservationRequest;
import com.example.booking_room.reservation.controller.data.JsonGetReservationListResponse;
import com.example.booking_room.reservation.controller.data.JsonReservationResponse;
import com.example.booking_room.reservation.service.ReservationService;
import com.example.booking_room.room.controller.data.JsonGetRoomListResponse;
import com.example.booking_room.room.service.RoomService;
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
    private final RoomService roomService;

    public ReservationController(@NonNull ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    @GetMapping(value = "/available_rooms")
    public ResponseEntity<?> getAllRoomsAvailable(@RequestBody @NonNull final CheckDate checkDate) {
//todo getAvailableRoomIds
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
    public ResponseEntity<?> delete(@PathVariable Integer reservationID) { // todo same with res entity
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
