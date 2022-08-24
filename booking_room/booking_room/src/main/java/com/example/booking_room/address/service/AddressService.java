package com.example.booking_room.address.service;

import com.example.booking_room.address.Address;
import com.example.booking_room.address.RegisterAddressRequest;
import com.example.booking_room.address.UpdateAddressRequest;
import com.example.booking_room.address.controller.data.JsonAddressResponse;
import com.example.booking_room.address.controller.data.JsonGetAddressListResponse;
import com.example.booking_room.address.repository.AddressRepository;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @NonNull
    private final AddressRepository addressRepository;

    public AddressService(@NonNull final AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public JsonAddressResponse registerAddress(@NonNull final RegisterAddressRequest registerAddressRequest) { // must return a JsonPerson

        final Address address = Address.builder()
                .city(registerAddressRequest.getCity())
                .street(registerAddressRequest.getStreet())
                .streetNumber(registerAddressRequest.getStreetNumber())
                .floor(registerAddressRequest.getFloor())
                .roomNumber(registerAddressRequest.getRoomNumber())
                .build();

        final Address registeredAddress = addressRepository.create(address);


        return JsonAddressResponse.toJson(registeredAddress);
    }

    public JsonAddressResponse getAddress(Integer addressID) {

        try {
            Address address = addressRepository.readByID(addressID);
            return JsonAddressResponse.toJson(address);
        } catch (Exception e) {
            throw new RuntimeException("Address with id:" + addressID + " not found!");
        }
    }

    public void deleteAddressByID(Integer addressID) {

        try {
            addressRepository.deleteByID(addressID);
        } catch (Exception e) {
            throw new RuntimeException("Address with :" + addressID + "not found");
        }

    }

    public JsonAddressResponse updateAddress(@NonNull final Integer addressID, @NonNull final UpdateAddressRequest updateAddressRequest) {
        System.out.println(addressID);

        final Address existingAddress = addressRepository.readByID(addressID);

        if (existingAddress == null) { //here throw exception
            throw new RuntimeException("The address with id: " + addressID + " doesn't exist. Please register a request to create a new person");
        }

        final Address.AddressBuilder addressUpdate = Address.builder();
        addressUpdate.addressID(addressID);
        if (updateAddressRequest.getCity() != null) {
            addressUpdate.city(updateAddressRequest.getCity());
        } else {
            addressUpdate.city(existingAddress.getCity());
            System.out.println("FirstName in address service:" + existingAddress.getCity());
        }
        if (updateAddressRequest.getStreet() != null) {
            addressUpdate.street(updateAddressRequest.getStreet());
        } else {
            addressUpdate.street(existingAddress.getStreet());
        }
        if (updateAddressRequest.getStreetNumber() != null) {
            addressUpdate.streetNumber(updateAddressRequest.getStreetNumber());
        } else {
            addressUpdate.streetNumber(existingAddress.getStreetNumber());
        }
        if (updateAddressRequest.getFloor() != null) {
            addressUpdate.floor(updateAddressRequest.getFloor());
        } else {
            addressUpdate.floor(existingAddress.getFloor());
        }
        if (updateAddressRequest.getRoomNumber() != null) {
            addressUpdate.roomNumber(updateAddressRequest.getRoomNumber());
        } else {
            addressUpdate.roomNumber(existingAddress.getRoomNumber());
        }

        final Address updatedAddress = addressUpdate.build();

        final Address updatedAddressResponse = addressRepository.update(updatedAddress);
        System.out.println("updatedAddress" + updatedAddress);

        return JsonAddressResponse.toJson(updatedAddressResponse);
    }

    public JsonGetAddressListResponse getAllAddresses() {

        //for (Address address : addressList) {
        //    System.out.println("AddressID: " + address.getAddressID() + " "
        //             + "FirstName:" + address.getFirstName() + " "
        //             + "LastName:" + address.getLastName() + " "
        //             + "Email:" + address.getEmail() + " "
        //             + "PhoneNumber:" + address.getPhoneNumber() + " "
        //             + "Role:" + address.getRole());
        //  }

        try {
            List<Address> addressList = addressRepository.readAll();
            return JsonGetAddressListResponse.builder().addresses(addressList //am trans un for intr un stream
                    .stream()
                    .map(address -> JsonAddressResponse.toJson(address))//map ul imi ia elem din lista de pers si le transf intr o jsonlista
                    .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("we can not show you the addresses from the list");
        }

    }
}
