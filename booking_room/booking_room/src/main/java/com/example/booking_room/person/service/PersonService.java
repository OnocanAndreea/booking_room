package com.example.booking_room.person.service;

import com.example.booking_room.person.Person;
import com.example.booking_room.person.RegisterPersonRequest;
import com.example.booking_room.person.UpdatePersonRequest;
import com.example.booking_room.person.controller.data.JsonGetPersonListResponse;
import com.example.booking_room.person.controller.data.JsonPersonResponse;
import com.example.booking_room.person.repository.PersonRepository;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @NonNull
    private final PersonRepository personRepository;

    public PersonService(@NonNull final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public JsonPersonResponse registerPerson(@NonNull final RegisterPersonRequest registerPersonRequest) { // must return a JsonPerson

        final Person person = Person.builder()
                .firstName(registerPersonRequest.getFirstName())
                .lastName(registerPersonRequest.getLastName())
                .phoneNumber(registerPersonRequest.getPhoneNumber())
                .email(registerPersonRequest.getEmail())
                .role(registerPersonRequest.getRole())
                .build();

        final Person registeredPerson = personRepository.create(person);


        return JsonPersonResponse.toJson(registeredPerson);
    }

    public JsonPersonResponse getPerson(Integer personID) {

        try {
            Person person = personRepository.readByID(personID);
            return JsonPersonResponse.toJson(person);
        } catch (Exception e) {
            throw new RuntimeException("Person with id:" + personID + " not found!");
        }
    }

    public void deletePersonByID(Integer personID) {

        try {
            personRepository.deleteByID(personID);
        } catch (Exception e) {
            throw new RuntimeException("Person with :" + personID + "not found");
        }

    }

    public JsonPersonResponse updatePerson(@NonNull final Integer personID, @NonNull final UpdatePersonRequest updatePersonRequest) {
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

        final Person updatedPersonResponse = personRepository.update(updatedPerson);
        System.out.println("updatedPerson" + updatedPerson);

        return JsonPersonResponse.toJson(updatedPersonResponse);
    }

    public JsonGetPersonListResponse getAllPersons() {

        //for (Person person : personList) {
        //    System.out.println("PersonID: " + person.getPersonID() + " "
        //             + "FirstName:" + person.getFirstName() + " "
        //             + "LastName:" + person.getLastName() + " "
        //             + "Email:" + person.getEmail() + " "
        //              + "PhoneNumber:" + person.getPhoneNumber() + " "
        //             + "Role:" + person.getRole());
        //  }

        try {
            List<Person> personList = personRepository.readAll();
            return JsonGetPersonListResponse.builder().persons(personList
                    .stream()
                    .map(person -> JsonPersonResponse.toJson(person))
                    .collect(Collectors.toList()))
                    .build();
            // not working
            /*personList.builder().persons(personList //am trans un for intr un stream
                    .stream()
                    .map(JsonPersonResponse::toJson)//map ul imi ia elem din lista de pers si le transf intr o jsonlista
                    .collect(Collectors.toList())).build();*/
        } catch (Exception e) {
            throw new RuntimeException("we can not show you the persons from the list");
        }

    }

}
