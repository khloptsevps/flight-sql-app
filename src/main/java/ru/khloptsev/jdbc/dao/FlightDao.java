package ru.khloptsev.jdbc.dao;

import ru.khloptsev.jdbc.entity.Flight;
import ru.khloptsev.jdbc.entity.FlightStatus;
import ru.khloptsev.jdbc.exceptions.DaoException;
import ru.khloptsev.jdbc.utils.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDao implements Dao<Integer, Flight> {
    private FlightDao() {}

    private static final FlightDao INSTANCE = new FlightDao();

    private static final String INSERT_SQL = """
            INSERT INTO flight(flight_number, departure_date, departure_airport_code,
                   arrival_date, arrival_airport_code, aircraft_id, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE flight
            set flight_number = ?,
                departure_date = ?,
                departure_airport_code = ?,
                arrival_date = ?,
                arrival_airport_code = ?,
                aircraft_id = ?,
                status = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM flight
            WHERE id = ?
            """;

    private static final String FIND_ALL = """
            select id, flight_number, departure_date, departure_airport_code,
                   arrival_date, arrival_airport_code, aircraft_id, status
            from flight
            """;

    private static final String FIND_BY_ID = FIND_ALL + """
            WHERE id = ?
            """;

    @Override
    public Flight create(Flight flight) {
        try(var connection = ConnectionPool.getConnection();
            var statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(flight, statement);

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                flight.setId(keys.getInt(1));
            }

            return flight;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Flight flight) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            setStatement(flight, statement);
            statement.setInt(8, flight.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void setStatement(Flight flight, PreparedStatement statement) throws SQLException {
        statement.setString(1, flight.getFlightNumber());
        statement.setTimestamp(2, Timestamp.valueOf(flight.getDepartureDate()));
        statement.setString(3, flight.getDepartureAirportCode());
        statement.setTimestamp(4, Timestamp.valueOf(flight.getArrivalDate()));
        statement.setString(5, flight.getArrivalAirportCode());
        statement.setInt(6, flight.getAircraftId());
        statement.setString(7, flight.getStatus().name());
    }

    @Override
    public Optional<Flight> findById(Integer id) {
        try(var connection = ConnectionPool.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Flight> findById(Integer id, Connection connection) {
        try(var statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Flight result = null;

            if (resultSet.next()) {
                result = buildFlight(resultSet);
            }
            return Optional.ofNullable(result);


        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Flight> findAll() {
        List<Flight> result = new ArrayList<>();
        try(var connection = ConnectionPool.getConnection();
            var statement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(buildFlight(resultSet));
            }
            return result;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    private Flight buildFlight(ResultSet resultSet) throws SQLException {
        Flight flight = null;
        flight = new Flight(
                resultSet.getInt("id"),
                resultSet.getString("flight_number"),
                resultSet.getTimestamp("departure_date").toLocalDateTime(),
                resultSet.getString("departure_airport_code"),
                resultSet.getTimestamp("arrival_date").toLocalDateTime(),
                resultSet.getString("arrival_airport_code"),
                resultSet.getInt("aircraft_id"),
                FlightStatus.valueOf(resultSet.getString("status"))
        );
        return flight;
    }
}
