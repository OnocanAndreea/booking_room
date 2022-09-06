package com.example.booking_room.room.repository;

import com.example.booking_room.room.Room;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Repository
public class RoomRepository {

    private final SessionFactory hibernateFactory;

    @Autowired
    public RoomRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @NonNull
    public Room create(@NonNull final Room room) {
        final RoomEntity roomEntity = toEntity(room);

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {

            transaction = session.beginTransaction();

            final Serializable roomId = session.save(roomEntity);

            transaction.commit();

            final RoomEntity savedRoomEntity = session.load(RoomEntity.class, roomId);

            return fromEntity(savedRoomEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public Room readByID(Integer roomID) {
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            RoomEntity roomEntity = session.get(RoomEntity.class, roomID);

            transaction.commit();

            return fromEntity(roomEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteByID(@NonNull Integer roomID) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            RoomEntity roomEntity = session.load(RoomEntity.class, roomID);

            if (roomEntity != null) {
                session.delete(roomEntity);
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
    public List<Room> readAll() {
        Transaction transaction = null;
        Session session = null;
        List<RoomEntity> roomEntityList;

        try {

            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            roomEntityList = session.createQuery("from RoomEntity ", RoomEntity.class).getResultList();
            transaction.commit();
            List<Room> roomList = new ArrayList<>();

            for (RoomEntity roomEntity : roomEntityList) {
                roomList.add(fromEntity(roomEntity));
            }
            return roomList;
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

    public @NonNull Room update(@NonNull final Room room) {
        final @NonNull RoomEntity roomEntity = toEntity(room);

        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(roomEntity);

            transaction.commit();
            return fromEntity(roomEntity);

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
    public Room fromEntity(@NonNull final RoomEntity roomEntity) {
        return Room.builder()
                .roomID(roomEntity.getRoomID())
                .numberOfSeats(roomEntity.getNumberOfSeats())
                .roomAddressID(roomEntity.getRoomAddressID())
                .type(roomEntity.getType())
                .build();
    }

    public static RoomEntity toEntity(@NonNull final Room room) {
        return RoomEntity.builder()
                .roomID(room.getRoomID())
                .numberOfSeats(room.getNumberOfSeats())
                .roomAddressID(room.getRoomAddressID())
                .type(room.getType())
                .build();

    }

}
