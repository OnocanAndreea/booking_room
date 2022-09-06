package com.example.booking_room.address.controller;

import com.example.booking_room.address.RegisterAddressRequest;
import com.example.booking_room.address.UpdateAddressRequest;
import com.example.booking_room.address.controller.data.JsonAddressResponse;
import com.example.booking_room.address.controller.data.JsonGetAddressListResponse;
import com.example.booking_room.address.service.AddressService;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("address")
public class AddressController {
    @NonNull
    private final AddressService addressService;

    public AddressController(@NonNull final AddressService addressService) {
        this.addressService = addressService;
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        try {
            JsonGetAddressListResponse jsonGetAddressListResponse = addressService.getAllAddresses();
            return ResponseEntity.ok(jsonGetAddressListResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }
    }

    @GetMapping(value = "/{addressID}")
    public ResponseEntity<?> getById(@PathVariable Integer addressID) {
        try {
            JsonAddressResponse jsonAddressResponse = addressService.getAddress(addressID);
            return ResponseEntity.ok(jsonAddressResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{addressID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer addressID) { // todo same with res entity
        try {
            addressService.deleteAddressByID(addressID);
            return ResponseEntity.ok("the address was deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody RegisterAddressRequest registerAddressRequest) { // todo resp entity 200

        try {
            JsonAddressResponse jsonAddressResponse = addressService.registerAddress(registerAddressRequest);
            return ResponseEntity.ok(jsonAddressResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((e.getMessage()));
        }

    }

    @PutMapping(value = "/{addressID}")
    public ResponseEntity<?> update(@RequestBody UpdateAddressRequest updateAddressRequest, @PathVariable Integer addressID) {
        System.out.println("Address with addressId:" + addressID + " to update");
        try {
            final JsonAddressResponse updatedAddress = addressService.updateAddress(addressID, updateAddressRequest);
            return ResponseEntity.ok(updatedAddress);
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
    }
}