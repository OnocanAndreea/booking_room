package com.example.booking_room.reservation.repository;

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
        final ReservationEntity reservationEntity = toEntity(reservation);

        Transaction transaction = null;

        try (Session session = hibernateFactory.openSession()) {

            transaction = session.beginTransaction();

            Query existingReservationsQuery = session.createQuery("from ReservationEntity where reservedRoomID = :reservedRoomID and arrivalDate >= :arrivalDate" +
                    " and departureDate <= :departureDate");

            existingReservationsQuery.setParameter("reservedRoomID", reservation.getReservedRoomID());
            existingReservationsQuery.setParameter("arrivalDate", reservation.getArrivalDate());
            existingReservationsQuery.setParameter("departureDate", reservation.getDepartureDate());

            if (reservation.getArrivalDate().isAfter(reservation.getDepartureDate())) {
                throw new RuntimeException("the dates aren't correct");
            }

            Reservation existingReservation = (Reservation) existingReservationsQuery.uniqueResult();

            if (existingReservation != null) {
                throw new RuntimeException("This room is already booked for the given date!");
            }

            final Serializable reservationId = session.save(reservationEntity);
            transaction.commit();
            final ReservationEntity savedReservationEntity = session.load(ReservationEntity.class, reservationId);
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

        List<ReservationEntity> reservationEntityList;
        try (Session session = hibernateFactory.openSession()) {
            // start a transaction
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

        }
    }

    @NonNull
    // this one simply inserts a new reservation
    public Reservation update(@NonNull final Reservation reservation) {
        final ReservationEntity reservationEntity = toEntity(reservation);

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            session.saveOrUpdate(reservationEntity); //this way it created a new row

            transaction.commit();
            return fromEntity(reservationEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
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

    public Reservation acceptOrDecline(Integer reservationID, boolean accepted) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            ReservationEntity reservationEntity = session.load(ReservationEntity.class, reservationID); //this way it created a new row
            reservationEntity.setAccepted(accepted);

            session.saveOrUpdate(reservationEntity);

            transaction.commit();
            return fromEntity(reservationEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
