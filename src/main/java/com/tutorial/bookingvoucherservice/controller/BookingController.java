package com.tutorial.bookingvoucherservice.controller;

import com.tutorial.bookingvoucherservice.modelo.Voucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tutorial.bookingvoucherservice.service.BookingService;
import com.tutorial.bookingvoucherservice.entity.BookingEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/")
    public ResponseEntity<List<BookingEntity>> listClient() {
        List<BookingEntity> booking = bookingService.getBooking();
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingEntity> getBookingById(@PathVariable Long id) {
        BookingEntity booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/rut/")
    public List<String> getRuts() {
        return bookingService.getRuts();
    }

    /*
    @GetMapping("/voucher/{id}")
    public ResponseEntity<VoucherEntity> getVoucherById(@PathVariable Long id) {
        VoucherEntity voucher = bookingService.getVoucherById(id);
        return ResponseEntity.ok(voucher);
    }
     */

    @PostMapping("/")
    public ResponseEntity<BookingEntity> saveBooking(@RequestBody BookingEntity booking) {
        BookingEntity bookingNew = bookingService.saveBooking(booking);
        return ResponseEntity.ok(bookingNew);
    }

    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBookingById(@PathVariable Long id) throws Exception {
        var isDeleted = bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
     */

    @PutMapping("/")
    public ResponseEntity<BookingEntity> updateBooking(@RequestBody BookingEntity booking){
        BookingEntity bookingUpdate = bookingService.updateBooking(booking);
        return ResponseEntity.ok(bookingUpdate);
    }

    @GetMapping("/voucher/{id}")
    public Voucher getVoucherById(@PathVariable Long id) {
        return bookingService.getVoucherById(id);
    }
}
