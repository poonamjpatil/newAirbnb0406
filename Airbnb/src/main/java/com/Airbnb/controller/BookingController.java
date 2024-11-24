package com.Airbnb.controller;

import com.Airbnb.dto.BookingDto;
import com.Airbnb.entity.Booking;
import com.Airbnb.entity.Property;
import com.Airbnb.entity.PropertyUser;
import com.Airbnb.repository.BookingRepository;
import com.Airbnb.repository.PropertyRepository;
import com.Airbnb.service.PDFService;
import com.Airbnb.service.SmsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private PDFService pdfService;
    private SmsService smsService;

    public BookingController(BookingRepository bookingRepository, PropertyRepository propertyRepository, PDFService pdfService, SmsService smsService) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.pdfService = pdfService;
        this.smsService = smsService;
    }

    @PostMapping("/createBooking/{propertyId}")
     public Object createBooking(
            @RequestBody Booking booking,
            @AuthenticationPrincipal PropertyUser user,
            @PathVariable long propertyId
    ){
       booking.setPropertyUser(user);
        Property property = propertyRepository.findById(propertyId).get();
        int propertyPrice = property.getNightlyPrice();
        int totalNights = booking.getTotalNights();
        int totalPrice = propertyPrice * totalNights;
        booking.setProperty(property);
        booking.setTotalPrice(totalPrice);
        Booking createdBooking = bookingRepository.save(booking);

        BookingDto dto = new BookingDto();
        dto.setBookingId(createdBooking.getId());
        dto.setGuestName(createdBooking.getGuestName());
        dto.setPrice(propertyPrice);
        dto.setTotalPrice(createdBooking.getTotalPrice());
        //create PDF with booking confirmation

        boolean b =pdfService.generatePDF("D://Preparation//Projects work//booking generated file//" + "booking-confirmation-id"+createdBooking.getId()+".pdf",dto);
        if(b){
            smsService.sendSMS("+917977467187", "Your Booking is confirm. Click for more information: ");
        }else {
            return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(createdBooking,HttpStatus.CREATED);

    }

}
