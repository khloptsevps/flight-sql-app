package ru.khloptsev.jdbc;

import ru.khloptsev.jdbc.dao.TicketDao;
import ru.khloptsev.jdbc.dto.TicketFilter;
import ru.khloptsev.jdbc.entity.Ticket;

import java.math.BigDecimal;

public class DaoPractice {
    static void main() {
        Ticket ticket = new Ticket(
                60,
                "2BC3AF",
                "John Doe",
                9, "A3",
                BigDecimal.valueOf(283.22)
        );


        TicketDao instance = TicketDao.getInstance();

        TicketFilter filter = new TicketFilter( null, null,3, 0);

        System.out.println(instance.findAll(filter));

    }
}
