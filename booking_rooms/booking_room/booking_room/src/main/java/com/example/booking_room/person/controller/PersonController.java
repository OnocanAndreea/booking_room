package com.example.booking_room.person.controller;

import com.example.booking_room.person.Person;
import com.example.booking_room.person.RegisterPersonRequest;
import com.example.booking_room.person.UpdatePersonRequest;
import com.example.booking_room.person.service.PersonService;
import lombok.NonNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//endpoint by default
@RequestMapping("person")
public class PersonController {
    @NonNull
    private final PersonService personService;

    public PersonController(@NonNull final PersonService personService) {
        this.personService = personService;
    }

    //works
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void getTest() {

        System.out.println("The test works");
    }

    //works
    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            personService.getAllPersons();
            return ResponseEntity.ok("the list of persons");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }
    }

    //works, but there is no Exception handled
    @GetMapping(value = "/{personID}")
    public ResponseEntity<?> getById(@PathVariable Integer personID) {
        try {
            Person person = personService.getPerson(personID);
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // works, but there is no Exception handled
    @RequestMapping(value = "/{personID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer personID) { // todo same with res entity
        try {
            personService.deletePersonByID(personID);
            return ResponseEntity.ok("the person was deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // works
    @RequestMapping(method = RequestMethod.POST)
    public Person add(@RequestBody RegisterPersonRequest registerPersonRequest) { // todo resp entity 200
        System.out.println("New person created: " + registerPersonRequest);
         personService.registerPerson(registerPersonRequest);
        return null;
    }

    // this works
    @PutMapping(value = "/{personID}")
    public ResponseEntity<?> update(@RequestBody UpdatePersonRequest updatePersonRequest, @PathVariable Integer personID) {
        System.out.println("Person with personId:" + personID + " to update");
        try {
            final Person updatedPerson = personService.updatePerson(personID, updatePersonRequest);
            return ResponseEntity.ok(updatedPerson);
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
    }
}
