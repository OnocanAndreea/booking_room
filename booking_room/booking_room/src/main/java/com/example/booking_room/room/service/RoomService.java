package com.example.booking_room.room.service;



import com.example.booking_room.person.Person;
import com.example.booking_room.person.RegisterPersonRequest;
import com.example.booking_room.person.UpdatePersonRequest;
import com.example.booking_room.person.repository.PersonRepository;
import com.example.booking_room.room.repository.RoomRepository;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @NonNull
    private final RoomRepository roomRepository;

    public RoomService(@NonNull RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room registerRoom (@NonNull final RegisterRoomRequest registerRoomRequest) { // must return a person ?
        //todo map registerRoomRequest to room
        final Room room = Room.builder()
                .roomID(registerRoomRequest.getRoomID())
                .numberOfSits(registerRoomRequest.getNumberOfSits())
                .roomAddressID(registerRoomRequest.getRoomAddressID())
                .type(registerRoomRequest.getType())
                .build();

        final Room registeredRoom = roomRepository.create(room);


        return room;
    }

    public Room getRoom(Integer roomID) {
        try {
            return roomID.readByID(roomID);
        } catch (Exception e) {
            throw new RuntimeException("Room with id:" + roomID + " not found!");
        }
    }

    public void deleteRoomByID(Integer roomID) {

        try {
            roomRepository.deleteByID(roomID);
        }catch (Exception e){
            throw new RuntimeException("Room with :" + roomID + "not found");
        }

    }

    public Room updateRoom(@NonNull final Integer roomID, @NonNull final UpdateRoomRequest updatePersonRequest) {
        System.out.println(roomID);
        // o problema este, ca desi eu am sters din db de exemplu un person, daca i-am dat idul la update person
        // si numai de exemplu la firstname ceva, tot mi-o luat valoriile vechi de la lastname etc,
        // adica mi-o dat update la ce am sters deja

        final Room existingRoom = roomRepository.readByID(roomID);

        if (existingRoom == null) { //here throw exception
            throw new RuntimeException("The room with id: " + roomID + " doesn't exist. Please register a request to create a new person");
        }

        final Room.RoomBuilder roomUpdate = Room.builder();
        roomUpdate.roomID(roomID);
        if (UpdateRoomRequest.getroomID() != null) {
            personUpdate.firstName(updatePersonRequest.getFirstName());
        } else {
            personUpdate.firstName(existingPerson.getFirstName());
            System.out.println("FirstName in person service:" + existingPerson.getFirstName());
        }
        if (updatePersonRequest.getLastName() != null) {
            personUpdate.lastName(updatePersonRequest.getLastName());
        } else {
            personUpdate.lastName(existingPerson.getLastName());
        }
        if (updatePersonRequest.getEmail() != null) {
            personUpdate.email(updatePersonRequest.getEmail());
        } else {
            personUpdate.email(existingPerson.getEmail());
        }
        if (updatePersonRequest.getPhoneNumber() != null) {
            personUpdate.phoneNumber(updatePersonRequest.getPhoneNumber());
        } else {
            personUpdate.phoneNumber(existingPerson.getPhoneNumber());
        }
        if (updatePersonRequest.getRole() != null) {
            personUpdate.role(updatePersonRequest.getRole());
        } else {
            personUpdate.role(existingPerson.getRole());
        }

        final Person updatedPerson = personUpdate.build();

        final Person updatedPersonResp = personRepository.update(updatedPerson);
        System.out.println("updatedPerson" + updatedPerson);

        return updatedPerson;
    }

    public ResponseEntity<?> getAllPersons() {
        List<Person> personList = personRepository.readAll();
        for (Person person : personList) {
            System.out.println("PersonID: " + person.getPersonID() + " "
                    + "FirstName:" + person.getFirstName() + " "
                    + "LastName:" + person.getLastName() + " "
                    + "Email:" + person.getEmail() + " "
                    + "Phonenumber:" + person.getPhoneNumber() + " "
                    + "Role:" + person.getRole());
        }

        try{
            personRepository.readAll();
        }catch (Exception e){
            throw new RuntimeException("Person with:" + personList +"not found");
        }
        return null;
    }
}

