package com.example.booking_room.room.service;


import com.example.booking_room.person.Person;
import com.example.booking_room.person.repository.PersonRepository;
import com.example.booking_room.room.RegisterRoomRequest;
import com.example.booking_room.room.Room;
import com.example.booking_room.room.UpdateRoomRequest;
import com.example.booking_room.room.controller.data.JsonGetRoomListResponse;
import com.example.booking_room.room.controller.data.JsonRoomResponse;
import com.example.booking_room.room.repository.RoomRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @NonNull
    private final RoomRepository roomRepository;
    @NonNull
    private final PersonRepository personRepository;

    public RoomService(@NonNull RoomRepository roomRepository, @NonNull PersonRepository personRepository) {
        this.roomRepository = roomRepository;
        this.personRepository=personRepository;
    }


    public JsonRoomResponse registerRoom(@NonNull final RegisterRoomRequest registerRoomRequest,@NonNull final Integer personID) { // must return a JsonRoomResponse
        //trebuie sa adauge o pers cu ID existent o camera, verific daca exista pers , si daca are rolul ca admin

        Person person = personRepository.readByID(personID);

        if (person == null) { //here throw exception
            throw new RuntimeException("The person with id: " + personID + " doesn't exist.");
        }
        String role = person.getRole();

        if (!role.equals("admin")){
            throw new RuntimeException("this person" + personID + " can't add a room");
        }

        final Room room = Room.builder()
                .numberOfSeats(registerRoomRequest.getNumberOfSeats())
                .roomAddressID(registerRoomRequest.getRoomAddressID())
                .type(registerRoomRequest.getType())
                .build();

        final Room registeredRoom = roomRepository.create(room);


        return JsonRoomResponse.toJson(registeredRoom);
    }

    public JsonRoomResponse getRoom(Integer roomID) {
        try {
            Room room = roomRepository.readByID(roomID);
            return JsonRoomResponse.toJson(room);
        } catch (Exception e) {
            throw new RuntimeException("Room with id:" + roomID + " not found!");
        }
    }

    public void deleteRoomByID(Integer roomID) {

        try {
            roomRepository.deleteByID(roomID);
        } catch (Exception e) {
            throw new RuntimeException("Room with :" + roomID + "not found");
        }

    }

    public JsonRoomResponse updateRoom(@NonNull final Integer roomID, @NonNull final UpdateRoomRequest updateRoomRequest) {
        System.out.println(roomID);

        final Room existingRoom = roomRepository.readByID(roomID);


        if (existingRoom == null) { //here throw exception
            throw new RuntimeException("The room with id: " + roomID + " doesn't exist. Please register a request to create a new person");
        }

        final Room.RoomBuilder roomUpdate = Room.builder();
        roomUpdate.roomID(roomID);
        if (updateRoomRequest.getRoomID() != null) {
            roomUpdate.roomID(updateRoomRequest.getRoomID());
        } else {
            roomUpdate.roomID(existingRoom.getRoomID());
            System.out.println(" Room ID:" + existingRoom.getRoomID());
        }
        if (updateRoomRequest.getNumberOfSeats() != null) {
            roomUpdate.numberOfSeats(updateRoomRequest.getNumberOfSeats());
        } else {
            roomUpdate.numberOfSeats(existingRoom.getNumberOfSeats());
        }
        if (updateRoomRequest.getRoomAddressID() != null) {
            roomUpdate.roomAddressID(updateRoomRequest.getRoomAddressID());
        } else {
            roomUpdate.roomAddressID(existingRoom.getRoomAddressID());
        }
        if (updateRoomRequest.getType() != null) {
            roomUpdate.type(updateRoomRequest.getType());
        } else {
            roomUpdate.type(existingRoom.getType());
        }

        final Room updatedRoom = roomUpdate.build();

        final Room updatedRoomResp = roomRepository.update(updatedRoom);
        System.out.println("updatedRoom" + updatedRoom);

        return JsonRoomResponse.toJson(updatedRoomResp);
    }

    public JsonGetRoomListResponse getAllRooms() {
        //for (Room room : roomList) {
        //     System.out.println("RoomID: " + room.getRoomID() + " "
        //             + "NumberOfSeats:" + room.getNumberOfSeats() + " "
        //             + "RoomAddressID:" + room.getRoomAddressID() + " "
        //             + "Type:" + room.getType() + " ");
        // }
        //roomList.
        //        forEach(room -> System.out.println("RoomID: " + room.getRoomID() + " "
        //                + "NumberOfSeats:" + room.getNumberOfSeats() + " "
        //                + "RoomAddressID:" + room.getRoomAddressID() + " "
        //                + "Type:" + room.getType() + " "));


        try {

            List<Room> roomList = roomRepository.readAll();
            return JsonGetRoomListResponse.builder().rooms(roomList
                    .stream()
                    .map(JsonRoomResponse::toJson)
                    .collect(Collectors.toList())).build();

        } catch (Exception e) {

            throw new RuntimeException("Room not found");
        }
    }
}

