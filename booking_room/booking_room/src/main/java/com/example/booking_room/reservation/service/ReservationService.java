package com.example.booking_room.reservation.service;


import com.example.booking_room.person.Person;
import com.example.booking_room.person.repository.PersonRepository;
import com.example.booking_room.reservation.CheckDate;
import com.example.booking_room.reservation.RegisterReservationRequest;
import com.example.booking_room.reservation.Reservation;
import com.example.booking_room.reservation.UpdateReservationRequest;
import com.example.booking_room.reservation.controller.data.JsonGetReservationListResponse;
import com.example.booking_room.reservation.controller.data.JsonReservationResponse;
import com.example.booking_room.reservation.repository.ReservationRepository;
import com.example.booking_room.room.Room;
import com.example.booking_room.room.controller.data.JsonGetRoomListResponse;
import com.example.booking_room.room.controller.data.JsonRoomResponse;
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

    public ReservationService(@NonNull final ReservationRepository reservationRepository, @NonNull final RoomRepository roomRepository, @NonNull PersonRepository personRepository)  {
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

        LocalDate date = LocalDate.parse(registerReservationRequest.getArrivalDate());//LocalDate to String conversion
        LocalDate date2 = LocalDate.parse(registerReservationRequest.getDepartureDate());

        final Reservation reservation = Reservation.builder()
                .numberOfInvitedPersons(registerReservationRequest.getNumberOfInvitedPersons())
                .arrivalDate(date)//here I added the conversion
                .departureDate(date2)
                .reservedRoomID(registerReservationRequest.getReservedRoomID())
                .organizerPersonID(registerReservationRequest.getOrganizerPersonID())
                .build();
        try {
            final Reservation registeredReservation = reservationRepository.create(reservation);
            return JsonReservationResponse.toJson(registeredReservation);
        } catch (Exception e) {
            throw new RuntimeException("registration could not be completed");
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

        LocalDate date = LocalDate.parse(updateReservationRequest.getArrivalDate());

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
        if (updateReservationRequest.getArrivalDate() != null) {
            reservationUpdate.arrivalDate(date);
        } else {
            reservationUpdate.arrivalDate(existingReservation.getArrivalDate());
        }
        if (updateReservationRequest.getDepartureDate() != null) {
            reservationUpdate.departureDate(date);
        } else {
            reservationUpdate.departureDate(existingReservation.getDepartureDate());
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
                            .map(JsonReservationResponse::toJson)
                            .collect(Collectors.toList()))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("we can not show you the reservations from the list");
        }

    }

    public JsonGetRoomListResponse getAllRoomsAvailable(@NonNull final CheckDate checkDate) {

        LocalDate fromDate = LocalDate.parse(checkDate.getFromDate());
        LocalDate toDate = LocalDate.parse(checkDate.getToDate());

        try {
            List<Reservation> availableRoomListIds = reservationRepository.readAll();
            List<Integer> collectedReservedRoomID = new ArrayList<>();
            for (Reservation reservation : availableRoomListIds) {
                if (reservation.getArrivalDate().compareTo(fromDate) > 0 && reservation.getDepartureDate().compareTo(toDate) < 0) {
                    collectedReservedRoomID.add(reservation.getReservedRoomID()); //todo availableroomsID return jsonresponse cu roomID
                }
            }
//            return JsonGetRoomListResponse.builder().rooms(availableRoomListIds
//                            .stream()
//                            .map(JsonRoomResponse::getRoomID)
//                            .collect(Collectors.toList()))
//                            .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
return null;
    }
}

