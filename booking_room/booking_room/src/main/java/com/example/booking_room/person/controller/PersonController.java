package com.example.booking_room.person.controller;

import com.example.booking_room.person.RegisterPersonRequest;
import com.example.booking_room.person.UpdatePersonRequest;
import com.example.booking_room.person.controller.data.JsonGetPersonListResponse;
import com.example.booking_room.person.controller.data.JsonPersonResponse;
import com.example.booking_room.person.service.PersonService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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

    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            JsonGetPersonListResponse jsonGetPersonListResponse = personService.getAllPersons();
            return ResponseEntity.ok(jsonGetPersonListResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }
    }

    @GetMapping(value = "/{personID}")
    public ResponseEntity<?> getById(@PathVariable Integer personID) {
        try {
            JsonPersonResponse jsonPersonResponse = personService.getPerson(personID);
            return ResponseEntity.ok(jsonPersonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{personID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer personID) {
        try {
            personService.deletePersonByID(personID);
            return ResponseEntity.ok("the person was deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody RegisterPersonRequest registerPersonRequest) {

        try {
            JsonPersonResponse jsonPersonResponse = personService.registerPerson(registerPersonRequest);
            return ResponseEntity.ok(jsonPersonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }

    }

    @PutMapping(value = "/{personID}")
    public ResponseEntity<?> update(@RequestBody UpdatePersonRequest updatePersonRequest, @PathVariable Integer personID) {
        System.out.println("Person with personId:" + personID + " to update");
        try {
            final JsonPersonResponse updatedPerson = personService.updatePerson(personID, updatePersonRequest);
            return ResponseEntity.ok(updatedPerson);
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
    }
}
