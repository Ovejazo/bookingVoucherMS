package com.tutorial.bookingvoucherservice.repository;

import com.tutorial.bookingvoucherservice.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
}
