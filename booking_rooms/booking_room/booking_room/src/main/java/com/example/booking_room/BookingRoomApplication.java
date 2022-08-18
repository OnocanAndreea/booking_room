package com.example.booking_;

import com.example.booking_.person.UpdatePersonRequest;
import com.example.booking_.person.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class BookingApplication {
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
//        final PersonService personService = context.getBean(PersonService.class);
     /* final RegisterPersonRequest registerPersonRequest = RegisterPersonRequest.builder()
                .email("ggg.kkk@email.com")
                .firstName("firstName7")
                .lastName("lastName7")
                .phoneNumber("phoneNumber7")
                .role("admin8")
                .build();
        personService.registerPerson(registerPersonRequest);*/
        //personService.deletePersonByID(3);
        //Person person=personService.getPerson(11);
        // personService.getAllPersons();
//       final UpdatePersonRequest updatePersonRequest = UpdatePersonRequest.builder()
//                .personID(1848)
//                .lastName("manupdatedfromcode")
//                .build();
//        personService.updatePerson(updatePersonRequest);
    }
}
