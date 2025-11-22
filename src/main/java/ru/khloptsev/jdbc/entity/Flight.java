package ru.khloptsev.jdbc.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight {
    private int id;
    private String flightNumber;
    private LocalDateTime departureDate;
    private String departureAirportCode;
    private LocalDateTime arrivalDate;
    private String arrivalAirportCode;
    private int aircraftId;
    private FlightStatus status;

    public Flight() {
    }

    public Flight(int id, String flightNumber, LocalDateTime departureDate,
                  String departureAirportCode, LocalDateTime arrivalDate, String arrivalAirportCode,
                  int aircraftId, FlightStatus status) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.departureDate = departureDate;
        this.departureAirportCode = departureAirportCode;
        this.arrivalDate = arrivalDate;
        this.arrivalAirportCode = arrivalAirportCode;
        this.aircraftId = aircraftId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public int getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureDate=" + departureDate +
                ", departureAirportCode='" + departureAirportCode + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", arrivalAirportCode='" + arrivalAirportCode + '\'' +
                ", aircraftId=" + aircraftId +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return id == flight.id && aircraftId == flight.aircraftId
                && Objects.equals(flightNumber, flight.flightNumber)
                && Objects.equals(departureDate, flight.departureDate)
                && Objects.equals(departureAirportCode, flight.departureAirportCode)
                && Objects.equals(arrivalDate, flight.arrivalDate)
                && Objects.equals(arrivalAirportCode, flight.arrivalAirportCode)
                && status == flight.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightNumber, departureDate, departureAirportCode,
                arrivalDate, arrivalAirportCode, aircraftId, status);
    }
}