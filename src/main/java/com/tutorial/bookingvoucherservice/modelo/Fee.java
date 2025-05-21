package com.tutorial.bookingvoucherservice.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fee {
    private int id;
    private int feeBase;
    private int bookingReservationMin;
    private int round;
}
