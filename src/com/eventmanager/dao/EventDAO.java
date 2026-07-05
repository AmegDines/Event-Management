package com.eventmanager.dao;

import com.eventmanager.database.DatabaseConnection;
import com.eventmanager.model.DayBasedEvent;
import com.eventmanager.model.Event;
import com.eventmanager.model.HourlyEvent;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public void addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (name, category, venue, start_date, end_date, start_time, end_time, description, status, event_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getCategory());
            pstmt.setString(3, event.getVenue());
            
            if (event instanceof DayBasedEvent) {
                DayBasedEvent de = (DayBasedEvent) event;
                pstmt.setString(4, de.getStartDate().toString());
                pstmt.setString(5, de.getEndDate().toString());
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
            } else if (event instanceof HourlyEvent) {
                HourlyEvent he = (HourlyEvent) event;
                pstmt.setString(4, he.getEventDate().toString());
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setString(6, he.getStartTime().toString());
                pstmt.setString(7, he.getEndTime().toString());
            }
            
            pstmt.setString(8, event.getDescription());
            pstmt.setString(9, event.getStatus());
            pstmt.setString(10, event.getEventType());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    event.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        }
        return events;
    }

    public void deleteEvent(int id) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String category = rs.getString("category");
        String venue = rs.getString("venue");
        String description = rs.getString("description");
        String status = rs.getString("status");
        String type = rs.getString("event_type");

        if ("DAY_BASED".equals(type)) {
            LocalDate startDate = LocalDate.parse(rs.getString("start_date"));
            LocalDate endDate = LocalDate.parse(rs.getString("end_date"));
            return new DayBasedEvent(id, name, category, venue, description, status, startDate, endDate);
        } else {
            LocalDate eventDate = LocalDate.parse(rs.getString("start_date"));
            LocalTime startTime = LocalTime.parse(rs.getString("start_time"));
            LocalTime endTime = LocalTime.parse(rs.getString("end_time"));
            return new HourlyEvent(id, name, category, venue, description, status, eventDate, startTime, endTime);
        }
    }
}
