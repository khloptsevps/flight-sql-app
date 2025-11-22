package ru.khloptsev.jdbc.dao;

import ru.khloptsev.jdbc.dto.TicketFilter;
import ru.khloptsev.jdbc.entity.Ticket;
import ru.khloptsev.jdbc.exceptions.DaoException;
import ru.khloptsev.jdbc.utils.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao implements Dao<Integer, Ticket> {
    private static final TicketDao INSTANCE = new TicketDao();

    private TicketDao() {
    }
    private static final FlightDao flightDao = FlightDao.getInstance();

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    private static final String SAVE_SQL = """
            insert into ticket(passport_number, passenger_name, flight_id, seat_number, cost)
            values (?, ?, ?, ?, ?)
            """;

    private static final String DELETE_SQL = """
            delete from ticket
            where id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT t.id, t.passport_number, t.passenger_name, t.flight_id,
                   t.seat_number, t.cost,
                   f.flight_number, f.departure_date, f.departure_airport_code,
                   f.arrival_date, f.arrival_airport_code, f.aircraft_id, f.status
            FROM ticket t
                JOIN flight f on t.flight_id = f.id
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            where t.id = ?
            """;

    private static final String UPDATE_TICKET_SQL = """
            update ticket
            set passport_number = ?,
                passenger_name = ?,
                flight_id = ?,
                seat_number = ?,
                cost = ?
            where id = ?
            """;

    public Ticket create(Ticket ticket) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ticket.getPassportNumber());
            statement.setString(2, ticket.getPassengerName());
            statement.setInt(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeatNumber());
            statement.setBigDecimal(5, ticket.getCost());

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                ticket.setId(keys.getInt(1));
            }

            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Integer id) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);


            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAll() {
        List<Ticket> result = new ArrayList<>();

        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(buildTicket(resultSet));
            }

            return result;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        List<Ticket> result = new ArrayList<>();

        if (filter.passengerName() != null) {
            parameters.add(filter.passengerName());
            whereSql.add("passenger_name = ?");
        }

        if (filter.seatNumber() != null) {
            parameters.add(filter.seatNumber());
            whereSql.add("seat_number = ?");
        }

        parameters.add(filter.limit());
        parameters.add(filter.offset());

        String condition = whereSql
                .stream()
                .collect(Collectors.joining(
                        " AND ",
                        whereSql.isEmpty() ? "" : " WHERE ",
                        " LIMIT ? OFFSET ?"));

        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(FIND_ALL_SQL + condition)) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(buildTicket(resultSet));
            }
            System.out.println(statement);
            return result;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Ticket> findById(Integer id) {
        System.out.println(id);
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Ticket result = null;

            if (resultSet.next()) {
                result = buildTicket(resultSet);
            }

            return Optional.ofNullable(result);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(Ticket ticket) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(UPDATE_TICKET_SQL)) {
            statement.setString(1, ticket.getPassportNumber());
            statement.setString(2, ticket.getPassengerName());
            statement.setInt(3, ticket.getFlight().getId());
            statement.setString(4, ticket.getSeatNumber());
            statement.setBigDecimal(5, ticket.getCost());
            statement.setInt(6, ticket.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Ticket buildTicket(ResultSet resultSet) throws SQLException {
//        Flight flight = new Flight(
//                resultSet.getInt("flight_id"),
//                resultSet.getString("flight_number"),
//                resultSet.getTimestamp("departure_date").toLocalDateTime(),
//                resultSet.getString("departure_airport_code"),
//                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
//                resultSet.getString("arrival_airport_code"),
//                resultSet.getInt("aircraft_id"),
//                FlightStatus.valueOf(resultSet.getString("status"))
//        );
        return new Ticket(
                resultSet.getInt("id"),
                resultSet.getString("passport_number"),
                resultSet.getString("passenger_name"),
                flightDao.findById(resultSet.getInt("flight_id"), resultSet.getStatement().getConnection())
                        .orElse(null),
                resultSet.getString("seat_number"),
                resultSet.getBigDecimal("cost")
        );
    }


}
