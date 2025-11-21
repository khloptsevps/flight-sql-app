# Flight-SQL-app

**Flight-SQL-app** — для практики SQL и интеграции с Java.  
В проекте реализована база данных авиакомпании с таблицами для аэропортов, самолётов, рейсов, посадочных мест и билетов.

---

## Структура базы данных

| Таблица    | Описание                                                            |
|------------|---------------------------------------------------------------------|
| `airport`  | Аэропорты: код, страна, город                                       |
| `aircraft` | Самолёты: модель, идентификатор                                     |
| `seat`     | Посадочные места: привязка к самолёту, уникальные номера            |
| `flight`   | Рейсы: номер рейса, даты вылета/прилёта, аэропорты, самолёт, статус |
| `ticket`   | Билеты: пассажир, рейс, место, стоимость                            |

**Связи:**

- `flight.departure_airport_code` / `flight.arrival_airport_code` → `airport.code`
- `flight.aircraft_id` → `aircraft.id`
- `seat.aircraft_id` → `aircraft.id`
- `ticket.flight_id` → `flight.id`
- Уникальность билета по `(flight_id, seat_number)`

---

## Тестовые данные (Для старта)
 - 4 аэропорта
 - 4 самолёта 8 посадочных мест
 - 9 рейсов с разными статусами
 - 55 пассажиров, есть как уникальные, так и те кто летает часто
 - Разные цены билетов

## Примеры SQL-запросов

**1. Длительность рейса в часах:**

```sql
SELECT flight_number,
       TIMESTAMPDIFF(HOUR, departure_date, arrival_date) AS duration_hours
FROM flight;
```

**2. Свободные места на рейс:**

```sql
SELECT f.id route_id, s.seat_number free_seat
FROM flight f
         JOIN aircraft ac ON f.aircraft_id = ac.id
         JOIN seat s ON ac.id = s.aircraft_id
         LEFT JOIN ticket t ON t.flight_id = f.id AND t.seat_number = s.seat_number
WHERE t.seat_number IS NULL
  AND f.id = 1;
```

---

## Запуск
1.	Создать базу данных (MySQL/PostgreSQL) 
2.	Выполнить скрипты создания таблиц и вставки тестовых данных (Таблицы нужно создать)
3. **SQL_tasks/init_flight_repo.sql** - нужно создать таблицы. В скрипте есть данные, помогут.
3.	Подключаться из Java-приложения через JDBC или через консоль
4.	Пиши и тестируй запросы
---

