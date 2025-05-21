package com.tutorial.bookingvoucherservice.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private int id;
    private String name;
    private String rut;
    private Integer cash;
    private Integer frecuency;
    private Date dateOfBirth;
}
