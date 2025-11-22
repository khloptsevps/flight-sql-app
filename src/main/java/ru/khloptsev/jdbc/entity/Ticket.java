package ru.khloptsev.jdbc.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Ticket {
    private int id;
    private String passportNumber;
    private String passengerName;
    private Flight flight;
    private String seatNumber;
    private BigDecimal cost;

    public Ticket() {
    }

    public Ticket(int id, String passportNumber, String passengerName,
                  Flight flight, String seatNumber, BigDecimal cost) {
        this.id = id;
        this.passportNumber = passportNumber;
        this.passengerName = passengerName;
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", passportNumber='" + passportNumber + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", flightId=" + flight +
                ", seatNumber='" + seatNumber + '\'' +
                ", cost=" + cost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && flight == ticket.flight
                && Objects.equals(passportNumber, ticket.passportNumber)
                && Objects.equals(passengerName, ticket.passengerName)
                && Objects.equals(seatNumber, ticket.seatNumber)
                && Objects.equals(cost, ticket.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passportNumber, passengerName, flight, seatNumber, cost);
    }
}
