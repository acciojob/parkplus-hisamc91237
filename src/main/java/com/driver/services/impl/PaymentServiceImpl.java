package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Payment payment = new Payment();
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        payment.setPaymentCompleted(false);
        payment.setReservation(reservation);
        PaymentMode paymentMode = null;
        if(mode.equalsIgnoreCase("card")) {
            paymentMode = PaymentMode.CARD;
        }
        else if (mode.equalsIgnoreCase("cash")) {
            paymentMode = PaymentMode.CASH;
        }
        else if (mode.equalsIgnoreCase("upi")) {
            paymentMode = PaymentMode.UPI;
        }
        if(paymentMode==null) {
            throw new Exception("Payment mode not detected");
        }
        payment.setPaymentMode(paymentMode);
        int bill = reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour();
        if(bill>amountSent) {
            throw new Exception("Insufficient Amount");
        }
        payment.setPaymentCompleted(true);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);
        return payment;
    }
}