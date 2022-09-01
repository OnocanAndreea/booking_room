package com.example.booking_room.reservation.repository;

import com.example.booking_room.reservation.CheckDate;
import com.example.booking_room.reservation.Reservation;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {

    public final SessionFactory hibernateFactory;

    @Autowired
    public ReservationRepository(EntityManagerFactory factory) {

        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @NonNull
    //works
    public Reservation create(@NonNull final Reservation reservation) // must return a Reservation
    {
        //am convertit obiectul in entity
        final ReservationEntity reservationEntity = toEntity(reservation);


        Transaction transaction = null;

        try ( Session session = hibernateFactory.openSession()){

            transaction = session.beginTransaction();
            //verific daca exista deja o rezervare pe camera aia si data aia

            Query existingReservationsQuery = session.createQuery("from ReservationEntity where reservedRoomID = :reservedRoomID and arrivalDate >= :arrivalDate" +
                    " and departureDate <= :departureDate");

            existingReservationsQuery.setParameter("reservedRoomID", reservation.getReservedRoomID());
            existingReservationsQuery.setParameter("arrivalDate",reservation.getArrivalDate());
            existingReservationsQuery.setParameter("departureDate",reservation.getDepartureDate());

            if(reservation.getArrivalDate().isAfter(reservation.getDepartureDate())){
                throw new RuntimeException("the dates aren't correct");
            }

            Reservation existingReservation = (Reservation) existingReservationsQuery.uniqueResult();

            if(existingReservation != null) {
                throw new RuntimeException("This room is already booked for the given date!");
            }

            //salvez o persoana in db
            final Serializable reservationId = session.save(reservationEntity);
            //am adaugat persoana cu succes daca nu e ok intram in catch
            transaction.commit();
            //de parca am executa un select(verific persoana in db daca o aparut)
            final ReservationEntity savedReservationEntity = session.load(ReservationEntity.class, reservationId);
            //convertesc entityul in pojo
            return fromEntity(savedReservationEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    //works
    public Reservation readByID(Integer reservationID) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            ReservationEntity reservationEntity = session.get(ReservationEntity.class, reservationID);

            System.out.println("Getting reservationEntity by reservationID:" + reservationID);

            transaction.commit();

            return fromEntity(reservationEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;

        }
    }

    //works
    public void deleteByID(@NonNull Integer reservationID) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            ReservationEntity reservationEntity = session.load(ReservationEntity.class, reservationID);

            System.out.println("in delete byid reservationentity:" + reservationEntity);

            if (reservationEntity != null) {
                session.delete(reservationEntity);
                transaction.commit();
            } else {
                transaction.rollback();
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                throw e;
            }

        }

    }

    @NonNull
    public List<Reservation> readAll() {

        Transaction transaction = null;
        Session session = null;

        List<ReservationEntity> reservationEntityList;
        try {
            // start a transaction
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();
            // commit transaction
            reservationEntityList = session.createQuery("from ReservationEntity ", ReservationEntity.class).getResultList();
            transaction.commit();

            return reservationEntityList
                    .stream()
                    .map(ReservationRepository::fromEntity)
                    .collect(Collectors.toList());


        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @NonNull
    // this one simply inserts a new reservation
    public Reservation update(@NonNull final Reservation reservation)
    {
        System.out.println("in update got reservation:" + reservation);

        final ReservationEntity reservationEntity = toEntity(reservation);
        System.out.println("in update reservationentity:" + reservationEntity);


        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(reservationEntity); //this way it created a new row

            transaction.commit();
            return fromEntity(reservationEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @NonNull
    public static Reservation fromEntity(@NonNull final ReservationEntity reservationEntity) {
        return Reservation.builder()
                .reservationID(reservationEntity.getReservationID())
                .numberOfInvitedPersons(reservationEntity.getNumberOfInvitedPersons())
                .arrivalDate(reservationEntity.getArrivalDate())
                .departureDate(reservationEntity.getDepartureDate())
                .reservedRoomID(reservationEntity.getReservedRoomID())
                .organizerPersonID(reservationEntity.getOrganizerPersonID())
                .build();
    }

    @NonNull
    private static ReservationEntity toEntity(@NonNull final Reservation reservation) {
        return ReservationEntity.builder()
                .reservationID(reservation.getReservationID())
                .numberOfInvitedPersons(reservation.getNumberOfInvitedPersons())
                .departureDate(reservation.getDepartureDate())
                .arrivalDate(reservation.getArrivalDate())
                .reservedRoomID(reservation.getReservedRoomID())
                .organizerPersonID(reservation.getOrganizerPersonID())
                .build();
    }
}
