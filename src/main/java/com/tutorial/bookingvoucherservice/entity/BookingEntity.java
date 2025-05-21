package com.tutorial.bookingvoucherservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    //El codigo de la reserva ¿Sera necesario?
    private Integer codigo;

    /*
     * Le fecha y hora de la reserva
     * hay que considerar que la hora de la reserva tiene que estar dentro del horario. además
     * Hay que considerar se refiere a la hora que se hizo la reserva, no la hora de la carrera.
     */
    private Date dateBooking;

    //Este sera el tiempo inicial de la duración de la reserva.
    private Date initialTime;

    //Este sera el tiempo final de la duración de la reserva
    private Date finalTime;

    //La cantidad de personas en la reserva
    private Integer numberOfPerson;

    //Tiempo limite de la reserva
    private Integer limitTime;

    //Nombre de la persona que hizo la reserva (Debe existir)
    private String mainPerson;

    //Rut de la persona que hizo la reserva (debe existir)
    private String personRUT;

    //La opción de tarifa
    private Integer optionFee;

    //Opción de día especial
    private Boolean especialDay;
}


