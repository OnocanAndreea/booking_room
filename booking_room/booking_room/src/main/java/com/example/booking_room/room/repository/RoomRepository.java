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
    public RoomRepository(EntityManagerFactory factory){
        if (factory.unwrap(SessionFactory.class) == null){
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @NonNull

    public Room create(@NonNull final Room room){ // must return a Room
        final RoomEntity roomEntity = toEntity(room);

        Transaction transaction = null;
        try(Session session = hibernateFactory.openSession()){

            transaction = session.beginTransaction();

            //salvez un room in db
            final Serializable roomId = session.save(roomEntity);

            //am adaugat camera cu succes daca nu intram in catch
            transaction.commit();

            //verific roomul in db sa vad daca o aparut(ca si cand as face un select in sql)
            final RoomEntity savedRoomEntity = session.load(RoomEntity.class, roomId);

            //convertesc entityul in pojo
            return fromEntity(savedRoomEntity);

        }catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public Room readByID(Integer roomID) {
        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            RoomEntity roomEntity = session.get(RoomEntity.class, roomID);

            System.out.println("Getting roomEntity by roomID:" + roomID);

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
        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            RoomEntity roomEntity = session.load(RoomEntity.class, roomID);

            System.out.println("in delete by id roomentity:" + roomEntity);

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
    public  List<Room> readAll() {
        Transaction transaction = null;
        Session session = null;
        List<RoomEntity> roomEntityList = new ArrayList<RoomEntity>();

        try {
            // start a transaction
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();
            // commit transaction
            roomEntityList = session.createQuery("from RoomEntity ", RoomEntity.class).getResultList();
            transaction.commit();
            List<Room> roomList = new ArrayList<Room>();
            // iti converteste un entitty in object
            for (RoomEntity roomEntity : roomEntityList) {
                roomList.add(fromEntity(roomEntity));
            }
            return roomList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
    // this one simply inserts a new person
    public @NonNull Room update(@NonNull final Room room) // must return a Person?
    {
        System.out.println("in update got person:" + room);
        // todo: map Person to RoomEntity
        final @NonNull RoomEntity roomEntity = toEntity(room);
        System.out.println("in update roomentity:" + roomEntity);

        // todo: use Hibernate EntityManager or Session to persist the entity in DB
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(roomEntity); //this way it created a new row
            //session.update(personEntity);
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


    //entity to domain object
    @NonNull
    public Room fromEntity(@NonNull final RoomEntity roomEntity){
        return Room.builder()
                .roomID(roomEntity.getRoomID())
                .numberOfSeats(roomEntity.getNumberOfSeats())
                .roomAddressID(roomEntity.getRoomAddressID())
                .type(roomEntity.getType())
                .build();
    }

    public  static RoomEntity toEntity(@NonNull final Room room){
        return RoomEntity.builder()
                .roomID(room.getRoomID())
                .numberOfSeats(room.getNumberOfSeats())
                .roomAddressID(room.getRoomAddressID())
                .type(room.getType())
                .build();

    }


}
