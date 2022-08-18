package com.example.booking_room.person.service;

import com.example.booking_room.person.Person;
import com.example.booking_room.person.RegisterPersonRequest;
import com.example.booking_room.person.UpdatePersonRequest;
import com.example.booking_room.person.repository.PersonRepository;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @NonNull
    private final PersonRepository personRepository;

    public PersonService(@NonNull final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person registerPerson(@NonNull final RegisterPersonRequest registerPersonRequest) { // must return a person ?
        //todo map registerPersonRequest to Person
        final Person person = Person.builder()
                .firstName(registerPersonRequest.getFirstName())
                .lastName(registerPersonRequest.getLastName())
                .phoneNumber(registerPersonRequest.getPhoneNumber())
                .email(registerPersonRequest.getEmail())
                .role(registerPersonRequest.getRole())
                .build();

        final Person registeredPerson = personRepository.create(person);


        return person;
    }

    public Person getPerson(Integer personID) {
        try {
            return personRepository.readByID(personID);
        } catch (Exception e) {
            throw new RuntimeException("Person with id:" + personID + " not found!");
        }
    }

    public void deletePersonByID(Integer personID) {

        try {
            personRepository.deleteByID(personID);
        }catch (Exception e){
            throw new RuntimeException("Person with :" + personID + "not found");
        }

    }

    public Person updatePerson(@NonNull final Integer personID, @NonNull final UpdatePersonRequest updatePersonRequest) {
        System.out.println(personID);
        // o problema este, ca desi eu am sters din db de exemplu un person, daca i-am dat idul la update person
        // si numai de exemplu la firstname ceva, tot mi-o luat valoriile vechi de la lastname etc,
        // adica mi-o dat update la ce am sters deja

        final Person existingPerson = personRepository.readByID(personID);

        if (existingPerson == null) { //here throw exception
            throw new RuntimeException("The person with id: " + personID + " doesn't exist. Please register a request to create a new person");
        }

        final Person.PersonBuilder personUpdate = Person.builder();
        personUpdate.personID(personID);
        if (updatePersonRequest.getFirstName() != null) {
            personUpdate.firstName(updatePersonRequest.getFirstName());
        } else {
            personUpdate.firstName(existingPerson.getFirstName());
            System.out.println("FirstName in person service:" + existingPerson.getFirstName());
        }
        if (updatePersonRequest.getLastName() != null) {
            personUpdate.lastName(updatePersonRequest.getLastName());
        } else {
            personUpdate.lastName(existingPerson.getLastName());
        }
        if (updatePersonRequest.getEmail() != null) {
            personUpdate.email(updatePersonRequest.getEmail());
        } else {
            personUpdate.email(existingPerson.getEmail());
        }
        if (updatePersonRequest.getPhoneNumber() != null) {
            personUpdate.phoneNumber(updatePersonRequest.getPhoneNumber());
        } else {
            personUpdate.phoneNumber(existingPerson.getPhoneNumber());
        }
        if (updatePersonRequest.getRole() != null) {
            personUpdate.role(updatePersonRequest.getRole());
        } else {
            personUpdate.role(existingPerson.getRole());
        }

        final Person updatedPerson = personUpdate.build();

        final Person updatedPersonResp = personRepository.update(updatedPerson);
        System.out.println("updatedPerson" + updatedPerson);

        return updatedPerson;
    }

    public ResponseEntity<?> getAllPersons() {
        List<Person> personList = personRepository.readAll();
        for (Person person : personList) {
            System.out.println("PersonID: " + person.getPersonID() + " "
                    + "FirstName:" + person.getFirstName() + " "
                    + "LastName:" + person.getLastName() + " "
                    + "Email:" + person.getEmail() + " "
                    + "Phonenumber:" + person.getPhoneNumber() + " "
                    + "Role:" + person.getRole());
        }

        try{
            personRepository.readAll();
        }catch (Exception e){
            throw new RuntimeException("Person with:" + personList +"not found");
        }
        return null;
    }
}
