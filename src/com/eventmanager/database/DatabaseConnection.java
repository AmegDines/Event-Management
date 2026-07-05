package com.eventmanager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:event_manager.db";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Enable foreign keys in SQLite
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            // Events table
            String createEvents = "CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "venue TEXT NOT NULL," +
                    "start_date TEXT," + // YYYY-MM-DD
                    "end_date TEXT," + // YYYY-MM-DD
                    "start_time TEXT," + // HH:MM
                    "end_time TEXT," + // HH:MM
                    "description TEXT," +
                    "status TEXT NOT NULL," +
                    "event_type TEXT NOT NULL" + // DAY_BASED or HOURLY
                    ");";
                    
            // Participants table
            String createParticipants = "CREATE TABLE IF NOT EXISTS participants (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "phone TEXT" +
                    ");";
                    
            // Registrations table
            String createRegistrations = "CREATE TABLE IF NOT EXISTS registrations (" +
                    "event_id INTEGER," +
                    "participant_id INTEGER," +
                    "registration_date TEXT," +
                    "PRIMARY KEY (event_id, participant_id)," +
                    "FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE" +
                    ");";
                    
            stmt.execute(createEvents);
            stmt.execute(createParticipants);
            stmt.execute(createRegistrations);
            
            System.out.println("Database initialized successfully.");
            
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
