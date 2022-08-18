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

@Repository
public class PersonRepository {

    private final SessionFactory hibernateFactory;

    @Autowired
    public PersonRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.hibernateFactory = factory.unwrap(SessionFactory.class);
    }

    @NonNull
    //works
    public Person create(@NonNull final Person person) // must return a Person
    {
        // todo: map(setez valorile in field) Person to PersonEntity
        //am convertit obiectul in entity
        final PersonEntity personEntity = toEntity(person);

        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            //salvez o persoana in db
            final Serializable personId = session.save(personEntity);
            //am adaugat persoana cu succes daca nu e ok intram in catch
            transaction.commit();
            //de parca am executa un select(verific persoana in db daca o aparut)
            final PersonEntity savedPersonEntity = session.load(PersonEntity.class, personId);
            //convertesc entityul in pojo
            return fromEntity(savedPersonEntity);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    //works
    public Person readByID(Integer personID) {
        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            PersonEntity personEntity = session.get(PersonEntity.class, personID);

            System.out.println("Getting personEntity by personID:" + personID);

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
        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();

            PersonEntity personEntity = session.load(PersonEntity.class, personID);

            System.out.println("in delete byid personentity:" + personEntity);

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
    public  List<Person> readAll() {
        Transaction transaction = null;
        Session session = null;
        List<PersonEntity> personEntityList = new ArrayList<PersonEntity>();

        try {
            // start a transaction
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();
            // commit transaction
            personEntityList = session.createQuery("from PersonEntity ", PersonEntity.class).getResultList();
            transaction.commit();
            List<Person> personList = new ArrayList<Person>();
            // iti converteste un entitty in object
            for (PersonEntity personEntity : personEntityList) {
                personList.add(fromEntity(personEntity));
            }
            return personList;
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

    @NonNull
    // this one simply inserts a new person
    public Person update(@NonNull final Person person) // must return a Person?
    {
        System.out.println("in update got person:" + person);
        // todo: map Person to PersonEntity
        final PersonEntity personEntity = toEntity(person);
        System.out.println("in update personentity:" + personEntity);

        // todo: use Hibernate EntityManger or Session to persist the entity in DB
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateFactory.openSession();
            transaction = session.beginTransaction();

            session.saveOrUpdate(personEntity); //this way it created a new row
            //session.update(personEntity);
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

    //todo entity to domain object
    @NonNull
    private static Person fromEntity(@NonNull final PersonEntity personEntity) {
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
