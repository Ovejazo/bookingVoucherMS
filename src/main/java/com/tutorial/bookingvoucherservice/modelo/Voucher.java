package com.tutorial.bookingvoucherservice.modelo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    private Long id;

    public String name;
    public String rut;
    public Integer fee;
    public Integer iva;
    public Double Discount;
    public Date dateBooking;
}
