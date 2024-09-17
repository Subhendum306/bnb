package com.bnb.controller;

import com.bnb.entity.AppUser;
import com.bnb.entity.Booking;
import com.bnb.entity.Property;
import com.bnb.entity.Room;
import com.bnb.repository.BookingRepository;
import com.bnb.repository.PropertyRepository;
import com.bnb.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/booking")
public class BookingController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    public BookingController(RoomRepository roomRepository, PropertyRepository propertyRepository) {
        this.roomRepository = roomRepository;
        this.propertyRepository = propertyRepository;
    }
    @PostMapping("/createBooking")
    public ResponseEntity<?> createBooking(
            @RequestParam long propertyId,
            @RequestParam String roomType,
            @RequestBody Booking booking,
            @AuthenticationPrincipal AppUser user
    ){
        Room room = roomRepository.findByPropertyIdAndType(propertyId, roomType);
        Property property = propertyRepository.findById(propertyId).get();
        if(room.getCount()==0){
            return new ResponseEntity<>("No rooms available", HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            float nightlyPrice=room.getPrice();
            float totalPrice=nightlyPrice*booking.getTotalNights();
            booking.setTotal_price(totalPrice);
            booking.setProperty(property);
            booking.setAppUser(user);
            booking.setTypeOfRoom(roomType);
            Booking savedBooking = bookingRepository.save(booking);

            if(savedBooking!=null){
                //Only After booking is done.
                int val=room.getCount();
                room.setCount(val-1);
                roomRepository.save(room);
            }
            return new ResponseEntity<>("RoomBooked",HttpStatus.CREATED);
        }
    }

}
