package ru.khloptsev.jdbc;

import ru.khloptsev.jdbc.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcPractice {
    static void main() {

        LocalDateTime start = LocalDate.of(2020, 6, 14).atStartOfDay();
        LocalDateTime end = LocalDate.of(2020,8,1).atStartOfDay();
        LocalDateTime depDate = LocalDate.of(2020, 7,28).atStartOfDay();

    }


    public static List<Long> getTicketByFlightId(Long id) {
        List<Long> result = new ArrayList<>();
        String sql = """
                select * from ticket
                where flight_id = ?
                """;


        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                result.add(rs.getLong("id"));
            }


        } catch (ExceptionInInitializerError e) {
            System.err.println("Error pool init: " + e.getCause().getMessage());
            System.exit(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) {
        List<Long> flights = new ArrayList<>();

        String sqlQuery = """
                select * from flight
                where departure_date between ? and ?;
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setTimestamp(1, Timestamp.valueOf(start));
            statement.setTimestamp(2, Timestamp.valueOf(end));


            var resultSet = statement.executeQuery();
            while(resultSet.next()) {
                flights.add(resultSet.getLong("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return flights;
    }

    public static List<String> getPassengerNames(String arrivalCity, LocalDateTime date) {
        List<String> passengers = new ArrayList<>();

        String sqlQuery = """
                select * from ticket t
                    join flight f on f.id = t.flight_id
                    join airport a on a.code = f.arrival_airport_code
                    where DATE(f.departure_date) = DATE(?) and a.city = ?
                """;

        try (var con = ConnectionManager.getConnection(); var ps = con.prepareStatement(sqlQuery)) {
            ps.setTimestamp(1, Timestamp.valueOf(date));
            ps.setString(2, arrivalCity);

            ResultSet result = ps.executeQuery();
            while(result.next()) {
                passengers.add(result.getString(3));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return passengers;
    }
}
