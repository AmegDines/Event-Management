package com.eventmanager.dao;

import com.eventmanager.database.DatabaseConnection;
import com.eventmanager.model.Participant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDAO {

    public void addParticipant(Participant participant) throws SQLException {
        String sql = "INSERT INTO participants (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, participant.getName());
            pstmt.setString(2, participant.getEmail());
            pstmt.setString(3, participant.getPhone());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    participant.setId(rs.getInt(1));
                }
            }
        }
    }

    public void updateParticipant(Participant participant) throws SQLException {
        String sql = "UPDATE participants SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, participant.getName());
            pstmt.setString(2, participant.getEmail());
            pstmt.setString(3, participant.getPhone());
            pstmt.setInt(4, participant.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteParticipant(int id) throws SQLException {
        String sql = "DELETE FROM participants WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Participant> getAllParticipants() throws SQLException {
        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT * FROM participants";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                participants.add(new Participant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        }
        return participants;
    }

    public void registerParticipantToEvent(int participantId, int eventId) throws SQLException {
        String sql = "INSERT INTO registrations (event_id, participant_id, registration_date) VALUES (?, ?, date('now'))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            pstmt.setInt(2, participantId);
            pstmt.executeUpdate();
        }
    }
}
