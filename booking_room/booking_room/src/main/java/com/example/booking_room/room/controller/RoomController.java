package com.example.booking_room.room.controller;

import com.example.booking_room.room.RegisterRoomRequest;
import com.example.booking_room.room.UpdateRoomRequest;
import com.example.booking_room.room.controller.data.JsonGetRoomListResponse;
import com.example.booking_room.room.controller.data.JsonRoomResponse;
import com.example.booking_room.room.service.RoomService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//endpoint by default
@RequestMapping("room")
public class RoomController {
    @NonNull
    private final RoomService roomService;

    public RoomController(@NonNull RoomService roomService) {
        this.roomService = roomService;
    }


    //works
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void getTest() {

        System.out.println("The test works");
    }

    //works
    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            JsonGetRoomListResponse jsonGetRoomListResponse = roomService.getAllRooms();
            return ResponseEntity.ok(jsonGetRoomListResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }
    }

    //works, but there is no Exception handled
    @GetMapping(value = "/{roomID}")
    public ResponseEntity<?> getById(@PathVariable Integer roomID) {

        try {

            JsonRoomResponse jsonRoomResponse = roomService.getRoom(roomID);
            return ResponseEntity.ok(jsonRoomResponse);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // works, but there is no Exception handled
    @RequestMapping(value = "/{roomID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer roomID) { // todo same with res entity

        try {
            roomService.deleteRoomByID(roomID);
            return ResponseEntity.ok("the room was deleted");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // works

    @RequestMapping(method = RequestMethod.POST, path = "/{personID}")
    public ResponseEntity<?> add(@RequestBody RegisterRoomRequest registerRoomRequest, @PathVariable Integer personID) { // todo resp entity 200

        try{
            JsonRoomResponse jsonRoomResponse = roomService.registerRoom(registerRoomRequest,personID);
            return ResponseEntity.ok(jsonRoomResponse);
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // this works
    @PutMapping(value = "/{roomID}")
    public ResponseEntity<?> update(@RequestBody UpdateRoomRequest updateRoomRequest, @PathVariable Integer roomID) {
        System.out.println("Room with personId:" + roomID + " to update");
        try {
            JsonRoomResponse jsonRoomResponse = roomService.updateRoom(roomID, updateRoomRequest);
            return ResponseEntity.ok(jsonRoomResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}