package com.example.booking_room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BookingRoomApplication {
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(BookingRoomApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
//        final PersonServicejhi personService = context.getBean(PersonServicejhi.class);
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
