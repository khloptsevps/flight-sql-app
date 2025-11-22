package ru.khloptsev.jdbc;

import ru.khloptsev.jdbc.dao.FlightDao;
import ru.khloptsev.jdbc.dao.TicketDao;
import ru.khloptsev.jdbc.dto.TicketFilter;
import ru.khloptsev.jdbc.entity.Flight;
import ru.khloptsev.jdbc.entity.FlightStatus;
import ru.khloptsev.jdbc.entity.Ticket;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;

public class DaoPractice {
    static void main() {
//        Ticket ticket = new Ticket(
//                60,
//                "2BC3AF",
//                "John Doe",
//                9, "A3",
//                BigDecimal.valueOf(283.22)
//        );
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Flight flight = new Flight(
                10,
                "BSL5004",
                LocalDateTime.parse("2020-05-22 03:35:00",  dtf),
                "BSL",
                LocalDateTime.parse("2020-05-22 06:45:00",  dtf),
                "LDN",
                2,
                FlightStatus.CANCELLED
        );
//
//
//        TicketDao tInstance = TicketDao.getInstance();
//
//        TicketFilter filter = new TicketFilter( null, null,3, 0);
//
//        System.out.println(instance.findAll(filter));

        FlightDao fInstance = FlightDao.getInstance();

//        System.out.println(tInstance.findById(3));

//        System.out.println(fInstance.create(flight));

//        System.out.println(fInstance.update(flight));

        System.out.println(fInstance.delete(10));
    }
}
