package com.example.booking_room.reservation.service;


import com.example.booking_room.person.Person;
import com.example.booking_room.person.repository.PersonRepository;
import com.example.booking_room.reservation.RegisterReservationRequest;
import com.example.booking_room.reservation.Reservation;
import com.example.booking_room.reservation.UpdateReservationRequest;
import com.example.booking_room.reservation.controller.data.JsonGetReservationListResponse;
import com.example.booking_room.reservation.controller.data.JsonReservationResponse;
import com.example.booking_room.reservation.repository.ReservationRepository;
import com.example.booking_room.room.Room;
import com.example.booking_room.room.repository.RoomRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @NonNull
    private final ReservationRepository reservationRepository;
    @NonNull
    private final RoomRepository roomRepository;

    @NonNull
    private final PersonRepository personRepository;

    public ReservationService(@NonNull final ReservationRepository reservationRepository, @NonNull RoomRepository roomRepository, @NonNull PersonRepository personRepository) throws Exception {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.personRepository = personRepository;
    }

    public JsonReservationResponse registerReservation(@NonNull final RegisterReservationRequest registerReservationRequest, @NonNull final Integer roomID,
                                                       @NonNull final Integer personID) { // must return a JsonPerson

        Person person = personRepository.readByID(personID);
        if (person == null) {
            throw new RuntimeException("this person isn't registered");
        }

        Room room = roomRepository.readByID(roomID);
        if (room == null) {
            throw new RuntimeException("the room is not exist");
        }

        Integer numberOfSeats = room.getNumberOfSeats();
        Integer numberOfInvitedPersons = registerReservationRequest.getNumberOfInvitedPersons();
        if (numberOfSeats < numberOfInvitedPersons) {
            throw new RuntimeException("the room is not big enough");
        }

        try {

            List<Reservation> reservationList = reservationRepository.readAll();
            List<Integer> collectedReservedRoomID = new ArrayList<>();
            for (Reservation reservation : reservationList) {
                String specificDate = registerReservationRequest.getDate();
                LocalDate date = LocalDate.parse(specificDate);
                if (date.compareTo(registerReservationRequest.getArrivalDate()) > 0 && date.compareTo(registerReservationRequest.getDepartureDate()) < 0) {
                    collectedReservedRoomID.add(reservation.getReservedRoomID());
                }
            }
//            List<Room> roomList = roomRepository.readAll();
//            for (Room room1 : roomList) {
//                System.out.printf("roomid from roomlist: " + room.getRoomID());
//                for (Integer roomID1 : collectedReservedRoomID) {
//                    if (roomID1 != room.getRoomID())
//                        System.out.println("roomid from list" + room.getRoomID());
//                }
//            }
//            List<Room> roomList2 = roomRepository.readAll();
//            for (Room room2 : roomList2) {
//                //  if(room.roomList2 == collectedReservedRoomID){
//                System.out.println();
//            }

        }catch (Exception e){
            throw new RuntimeException("error");
        }

//            List<Room> rooms = roomRepository.readAll();
//            for (Room room1 : rooms) {
//                roomids.add(room1.getRoomID());
//            }
//
//            List<Reservation> reservations = reservationRepository.readAll();
//            for (Reservation reservation1 : reservations) {
//                String specificDate = registerReservationRequest.getDate();
//                LocalDate getdate = LocalDate.parse(specificDate);
//                if (getdate.compareTo(registerReservationRequest.getFro()) >= 0) {
//                    roomids.add(reservation1.getReservationID());
//                }



        LocalDate date = LocalDate.parse(registerReservationRequest.getDate());//LocalDate to String conversion

        final Reservation reservation = Reservation.builder()
                .numberOfInvitedPersons(registerReservationRequest.getNumberOfInvitedPersons())
                .date(date)//here I added the conversion
                .reservedRoomID(registerReservationRequest.getReservedRoomID())
                .organizerPersonID(registerReservationRequest.getOrganizerPersonID())
                .build();
        try {
            final Reservation registeredReservation = reservationRepository.create(reservation);
            return JsonReservationResponse.toJson(registeredReservation);
        } catch (Exception e) {
            throw e;
        }
    }

    public JsonReservationResponse getReservation(Integer reservationID) {

        try {
            Reservation reservation = reservationRepository.readByID(reservationID);
            return JsonReservationResponse.toJson(reservation);
        } catch (Exception e) {
            throw new RuntimeException("Reservation with id:" + reservationID + " not found!");
        }
    }

    public void deleteReservationByID(Integer reservationID) {

        try {
            reservationRepository.deleteByID(reservationID);
        } catch (Exception e) {
            throw new RuntimeException("Reservation with :" + reservationID + "not found");
        }

    }

    public JsonReservationResponse updateReservation(@NonNull final Integer reservationID, @NonNull final UpdateReservationRequest updateReservationRequest) {
        System.out.println(reservationID);

        LocalDate date = LocalDate.parse(updateReservationRequest.getDate());

        if (!date.equals("dd-mm-yyyy")) {
            throw new RuntimeException("this date is not correct");
        }

        final Reservation existingReservation = reservationRepository.readByID(reservationID);

        if (existingReservation == null) { //here throw exception
            throw new RuntimeException("The reservation with id: " + reservationID + " doesn't exist. Please register a request to create a new person");
        }

        final Reservation.ReservationBuilder reservationUpdate = Reservation.builder();
        reservationUpdate.reservationID(reservationID);

        if (updateReservationRequest.getNumberOfInvitedPersons() != null) {
            reservationUpdate.numberOfInvitedPersons(updateReservationRequest.getNumberOfInvitedPersons());
        } else {
            reservationUpdate.numberOfInvitedPersons(existingReservation.getNumberOfInvitedPersons());
        }
        if (updateReservationRequest.getDate() != null) {
            reservationUpdate.date(date);
        } else {
            reservationUpdate.date(existingReservation.getDate());
        }
        if (updateReservationRequest.getReservedRoomID() != null) {
            reservationUpdate.reservedRoomID(updateReservationRequest.getReservedRoomID());
        } else {
            reservationUpdate.reservedRoomID(existingReservation.getReservedRoomID());
        }
        if (updateReservationRequest.getOrganizerPersonID() != null) {
            reservationUpdate.organizerPersonID(updateReservationRequest.getOrganizerPersonID());
        } else {
            reservationUpdate.organizerPersonID(existingReservation.getOrganizerPersonID());
        }

        final Reservation updatedReservation = reservationUpdate.build();

        final Reservation updatedReservationResponse = reservationRepository.update(updatedReservation);
        System.out.println("updatedReservation" + updatedReservation);

        return JsonReservationResponse.toJson(updatedReservationResponse);
    }

    public JsonGetReservationListResponse getAllReservations() {

        try {
            List<Reservation> reservationList = reservationRepository.readAll();
            return JsonGetReservationListResponse.builder().reservations(reservationList
                            .stream()
                            .map(reservation -> JsonReservationResponse.toJson(reservation))
                            .collect(Collectors.toList()))
                    .build();

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("we can not show you the reservations from the list");
        }

    }

}
