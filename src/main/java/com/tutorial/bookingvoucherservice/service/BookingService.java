package com.tutorial.bookingvoucherservice.service;

import com.tutorial.bookingvoucherservice.repository.BookingRepository;
import com.tutorial.bookingvoucherservice.entity.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.tutorial.bookingvoucherservice.modelo.Fee;
import com.tutorial.bookingvoucherservice.modelo.PersonNumber;
import com.tutorial.bookingvoucherservice.modelo.Client;
import com.tutorial.bookingvoucherservice.modelo.Frecuency;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;
/*
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    KartRepository kartRepository;



    @Autowired
    KartService kartService;
*/
    @Autowired
    RestTemplate restTemplate;

    public BookingEntity getBookingById(Long id){
        return bookingRepository.findById(id).get();
    }

    public ArrayList<BookingEntity> getBooking(){
        return (ArrayList<BookingEntity>) bookingRepository.findAll();
    }

    public BookingEntity updateBooking(BookingEntity booking) {
        return bookingRepository.save(booking);
    }


    public BookingEntity saveBooking(BookingEntity booking) {
        /*
         * Aquí la reserva se hará dependiendo de la tarifa que escoja el cliente
         */

        // Conseguimos al cliente que va a pagar.
        Client client = restTemplate.getForObject("http://clientMS/api/v1/clients/rut/" + booking.getPersonRUT(), Client.class);
        if (client == null) {
            throw new RuntimeException("Cliente no encontrado");
        }if(client.getCash() <= 0){
            throw new RuntimeException("El cliente no tiene dinero");
        }


        Fee fee = restTemplate.getForObject("http://feeMS/api/v1/fee/fee/" + booking.getOptionFee(), Fee.class);
        // Validamos si el cliente tiene suficiente dinero
        if (client.getCash() < fee.getFeeBase()) {
            throw new RuntimeException("El cliente no tiene suficiente saldo para realizar la reserva.");
        }

        // Colocamos el tiempo máximo de la reserva
        booking.setLimitTime(fee.getBookingReservationMin());

        // Vamos a pensar en los descuentos por grupo
        int nPersonas = booking.getNumberOfPerson();

        //Aquí van a ir todos los descuentos
        double descuentoFrecuencia = 0;
        double descuentoGrupo = 0;
        double descuentoCumplenos = 0;
        double descuentoDiaEspecial = 0;

        if (nPersonas >= 3 && nPersonas <= 5) descuentoGrupo = 0.10;
        else if (nPersonas >= 6 && nPersonas <= 10) descuentoGrupo = 0.20;
        else if (nPersonas >= 11 && nPersonas <= 15) descuentoGrupo = 0.30;

        // Descuento por frecuencia
        int visitasCliente = client.getFrecuency();


        if (visitasCliente >= 7) descuentoFrecuencia = 0.30;
        else if (visitasCliente >= 5) descuentoFrecuencia = 0.20;
        else if (visitasCliente >= 2) descuentoFrecuencia = 0.10;

        // Vamos a conseguir el cumpleaños del cliente

        boolean esCumpleanos = client.getDateOfBirth() == booking.getDateBooking();
        if ((esCumpleanos) && (nPersonas >= 3)) {
            if (nPersonas <= 5) descuentoCumplenos = 0.5;
        }

        // Aplicamos los descuentos
        double descuentoTotal = descuentoGrupo + descuentoFrecuencia + descuentoCumplenos + descuentoDiaEspecial;
        System.out.println("\nDescuento grupo: " + descuentoGrupo);
        System.out.println("\nDescuento Frecuencia: " + descuentoFrecuencia);
        System.out.println("\nDescuento Cumpleaños: " + descuentoCumplenos);
        System.out.println("\nDescuento día especial: " + descuentoDiaEspecial);
        double totalSinIVA = fee.getFeeBase() - (fee.getFeeBase() * descuentoTotal);

        //Vamos hacer el descuento en caso de que sea un día especial
        if(booking.getEspecialDay()){
            descuentoDiaEspecial = 0.05;
        }


        /*
         * Calculamos el IVA
         * Conseguimos el valor del IVA obteniendo el total multiplicado por 0.19
         * Luego le sumamos ese valor al total sin IVA obteniendo el total con el IVA incluido
         */
        double IVA = totalSinIVA * 0.19;
        double totalConIVA = totalSinIVA + IVA;

        client.setCash((int) (client.getCash() - totalConIVA));
        client.setFrecuency(client.getFrecuency() + 1); // o según políticas del negocio

        //aquí debería llamar a la función update?
        HttpEntity<Client> request = new HttpEntity<>(client);
        restTemplate.put("http://clientMS/api/v1/clients/", request);

        // Validar que el tiempo inicial esté configurado
        if (booking.getInitialTime() == null) {
            throw new RuntimeException("El tiempo inicial de la reserva no está definido.");
        }

        // Cálculo del tiempo final de la reserva
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(booking.getInitialTime());
        calendar.add(Calendar.MINUTE, booking.getLimitTime()); // Sumar los minutos de duración
        Date finalTime = calendar.getTime();

        // Establecer el tiempo final en la entidad de reserva
        booking.setFinalTime(finalTime);

        // Persistir la reserva
        return bookingRepository.save(booking);

        /*
        if(booking.getOptionFee() == 1){
            valorTarifa = 15000;
            /*
            * Hay que obtener al cliente y restarle el dinero y hacerle un descuento si es que es feriado
            * o si son muchas personas en un grupo


            //encuentro al cliente
            ClientEntity client = clientRepository.findByRut(booking.getPersonRUT());

            //Consigo el dinero del cliente
            client.setCash(client.getCash() - valorTarifa);

            //Se puede hacer un update para actualizar al cliente desde la reserva.
            clientService.updateClient(client);

            return bookingRepository.save(booking);

        //la tarifa vale 20.000
        } else if (booking.getOptionFee() == 2) {
            valorTarifa = 20000;


            return bookingRepository.save(booking);

        //La tarifa vale 25.000
        } else if (booking.getOptionFee() == 3) {
            valorTarifa = 25000;


            return bookingRepository.save(booking);
        }
        */

    }
/*
    //Funcion para borrar
    public boolean deleteBooking(Long id) throws Exception {
        try{
            bookingRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
    public VoucherEntity getVoucherById(Long id) {


        //Obtnemos la reserva con la que vamos a trabajar
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        //Obtenemos el cliente para conseguir su nombre y RUT
        ClientEntity client = clientRepository.findByRut(booking.getPersonRUT());
        if (client == null) {
            throw new RuntimeException("Cliente no encontrado");
        }

        //Creamos la instancia Voucher
        VoucherEntity voucher = new VoucherEntity();

        //Colocamos las cosas iniciales
        voucher.setName(booking.getMainPerson());
        voucher.setRut(booking.getPersonRUT());
        voucher.setDateBooking(booking.getDateBooking());

        // Calcular tarifa base según la opción
        int tarifaBase = 0;
        switch (booking.getOptionFee()) {
            case 1: tarifaBase = 15000; break;
            case 2: tarifaBase = 20000; break;
            case 3: tarifaBase = 25000; break;
            default: throw new RuntimeException("Opción de tarifa inválida");
        }
        voucher.setFee(tarifaBase);

        //Inicializamos los descuentos
        double descuentoTotal = 0.0;

        // Descuento por grupo
        int nPersonas = booking.getNumberOfPerson();
        if (nPersonas >= 3 && nPersonas <= 5) descuentoTotal += 0.10;
        else if (nPersonas >= 6 && nPersonas <= 10) descuentoTotal += 0.20;
        else if (nPersonas >= 11 && nPersonas <= 15) descuentoTotal += 0.30;

        // Descuento por frecuencia
        int visitasCliente = client.getFrecuency();
        if (visitasCliente >= 7) descuentoTotal += 0.30;
        else if (visitasCliente >= 5) descuentoTotal += 0.20;
        else if (visitasCliente >= 2) descuentoTotal += 0.10;

        // Descuento por cumpleaños
        boolean esCumpleanos = client.getDateOfBirth().equals(booking.getDateBooking());
        if (esCumpleanos && nPersonas >= 3 && nPersonas <= 5) {
            descuentoTotal += 0.5;
        }

        // Descuento por día especial
        if (booking.getEspecialDay()) {
            descuentoTotal += 0.05;
        }

        // Establecer descuento total
        voucher.setDiscount(descuentoTotal);

        // Calcular total sin IVA
        double totalSinIVA = tarifaBase * (1 - descuentoTotal);

        // Calcular IVA (19%)
        int iva = (int) (totalSinIVA * 0.19);
        voucher.setIva(iva);

        // Retornar el voucher creado
        return voucher;
    }
 */
}
