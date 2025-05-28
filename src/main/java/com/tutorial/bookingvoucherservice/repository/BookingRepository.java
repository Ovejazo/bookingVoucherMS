package com.tutorial.bookingvoucherservice.repository;

import com.tutorial.bookingvoucherservice.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("SELECT b.personRUT FROM BookingEntity b")
    List<String> findAllRuts();

}
