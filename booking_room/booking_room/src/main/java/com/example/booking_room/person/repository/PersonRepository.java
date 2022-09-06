package com.example.booking_room.person.repository;

import com.example.booking_room.person.Person;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PersonRepository {

    public final SessionFactory hibernateFactory;

    @Autowired
    public PersonRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @NonNull
    //works
    public Person create(@NonNull final Person person) {
        final PersonEntity personEntity = toEntity(person);

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            final Serializable personId = session.save(personEntity);
            transaction.commit();
            final PersonEntity savedPersonEntity = session.load(PersonEntity.class, personId);
            return fromEntity(savedPersonEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public Person readByID(Integer personID) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            PersonEntity personEntity = session.get(PersonEntity.class, personID);
            transaction.commit();
            return fromEntity(personEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    //works
    public void deleteByID(@NonNull Integer personID) {

        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            PersonEntity personEntity = session.load(PersonEntity.class, personID);

            if (personEntity != null) {
                session.delete(personEntity);
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
    public List<Person> readAll() {
        Transaction transaction = null;
        Session session = null;
        List<PersonEntity> personEntityList = new ArrayList<PersonEntity>();

        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            personEntityList = session.createQuery("from PersonEntity ", PersonEntity.class).getResultList();
            transaction.commit();
            List<Person> personList = new ArrayList<Person>();

            return personEntityList
                    .stream()
                    .map(PersonRepository::fromEntity)
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
    public Person update(@NonNull final Person person) {
        final PersonEntity personEntity = toEntity(person);
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(personEntity);
            transaction.commit();
            return fromEntity(personEntity);

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
    public static Person fromEntity(@NonNull final PersonEntity personEntity) {
        return Person.builder()
                .personID(personEntity.getPersonID())
                .firstName(personEntity.getFirstName())
                .lastName(personEntity.getLastName())
                .role(personEntity.getRole())
                .email(personEntity.getEmail())
                .phoneNumber(personEntity.getPhoneNumber())
                .build();
    }

    @NonNull
    private static PersonEntity toEntity(@NonNull final Person person) {
        return PersonEntity.builder()
                .personID(person.getPersonID())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .phoneNumber(person.getPhoneNumber())
                .role(person.getRole())
                .email(person.getEmail())
                .build();
    }
}
