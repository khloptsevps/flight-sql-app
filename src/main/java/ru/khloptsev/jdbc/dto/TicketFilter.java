package ru.khloptsev.jdbc.dto;

public record TicketFilter(
        String passengerName,
        String seatNumber,
        int limit,
        int offset) {
}
