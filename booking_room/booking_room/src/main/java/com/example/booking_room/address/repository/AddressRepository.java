package com.example.booking_room.address.repository;

import com.example.booking_room.address.Address;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
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
    public Address create(@NonNull final Address address)
    {
        final AddressEntity addressEntity = toEntity(address);

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            final Serializable addressId = session.save(addressEntity);
            transaction.commit();
            final AddressEntity savedAddressEntity = session.load(AddressEntity.class, addressId);
            return fromEntity(savedAddressEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public Address readByID(Integer addressID) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            AddressEntity addressEntity = session.get(AddressEntity.class, addressID);
            transaction.commit();

            return fromEntity(addressEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;

        }
    }

    public void deleteByID(@NonNull Integer addressID) {
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            AddressEntity addressEntity = session.load(AddressEntity.class, addressID);

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
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();
            addressEntityList = session.createQuery("from AddressEntity", AddressEntity.class).getResultList();
            transaction.commit();
            return addressEntityList
                    .stream()
                    .map(AddressRepository::fromEntity)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();

            }throw e;

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @NonNull

    public Address update(@NonNull final Address address)
    {
        final AddressEntity addressEntity = toEntity(address);

        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(addressEntity);
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
