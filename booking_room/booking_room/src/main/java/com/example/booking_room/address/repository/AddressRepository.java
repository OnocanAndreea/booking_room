package com.example.booking_room.address.repository;

import com.example.booking_room.address.Address;
import com.example.booking_room.person.Person;
import com.example.booking_room.person.repository.PersonEntity;
import com.example.booking_room.person.repository.PersonRepository;
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
import java.util.stream.Collectors;

@Repository
public class AddressRepository {

    final SessionFactory hibernateFactory;

    @Autowired
    public AddressRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @NonNull
    //works
    public Address create(@NonNull final Address address) // must return a Address
    {

        //am convertit obiectul in entity
        final AddressEntity addressEntity = toEntity(address);


        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            //salvez o persoana in db
            final Serializable addressId = session.save(addressEntity);
            //am adaugat persoana cu succes daca nu e ok intram in catch
            transaction.commit();
            //de parca am executa un select(verific persoana in db daca o aparut)
            final AddressEntity savedAddressEntity = session.load(AddressEntity.class, addressId);
            //convertesc entityul in pojo
            return fromEntity(savedAddressEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    //works
    public Address readByID(Integer addressID) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            AddressEntity addressEntity = session.get(AddressEntity.class, addressID);

            System.out.println("Getting addressEntity by addressID:" + addressID);

            transaction.commit();

            return fromEntity(addressEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;

        }
    }

    //works
    public void deleteByID(@NonNull Integer addressID) {
        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            AddressEntity addressEntity = session.load(AddressEntity.class, addressID);

            System.out.println("in delete by id addressentity:" + addressEntity);

            if (addressEntity != null) {
                session.delete(addressEntity);
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
    public List<Address> readAll() {
        Transaction transaction = null;
        Session session = null;
        List<AddressEntity> addressEntityList;
        try {
            // start a transaction
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();
            // commit transaction
            addressEntityList = session.createQuery("from AddressEntity", AddressEntity.class).getResultList();
            transaction.commit();
            return addressEntityList
                    .stream()
                    .map(AddressRepository::fromEntity)
                    .collect(Collectors.toList());


        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                throw e;
            }

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    @NonNull
    // this one simply inserts a new address
    public Address update(@NonNull final Address address) // must return a
    {
        System.out.println("in update got address:" + address);
        // todo: map Address to AddressEntity
        final AddressEntity addressEntity = toEntity(address);
        System.out.println("in update addressentity:" + addressEntity);

        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(addressEntity); //this way it created a new row
            //session.update(addressEntity);
            transaction.commit();
            return fromEntity(addressEntity);

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

    //todo entity to domain object
    @NonNull
    public static Address fromEntity(@NonNull final AddressEntity addressEntity) {
        return Address.builder()
                .addressID(addressEntity.getAddressID())
                .city(addressEntity.getCity())
                .street(addressEntity.getStreet())
                .streetNumber(addressEntity.getStreetNumber())
                .floor(addressEntity.getFloor())
                .roomNumber(addressEntity.getRoomNumber())
                .build();
    }

    @NonNull
    private static AddressEntity toEntity(@NonNull final Address address) {
        return AddressEntity.builder()
                .addressID(address.getAddressID())
                .city(address.getCity())
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .floor(address.getFloor())
                .roomNumber(address.getRoomNumber())
                .build();
    }
}
