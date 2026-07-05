package com.eventmanager.view;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.dao.ParticipantDAO;
import com.eventmanager.model.Event;
import com.eventmanager.model.Participant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ParticipantManagementPanel extends JPanel {
    private JTable participantTable;
    private DefaultTableModel tableModel;
    private ParticipantDAO participantDAO;
    private EventDAO eventDAO;

    public ParticipantManagementPanel() {
        participantDAO = new ParticipantDAO();
        eventDAO = new EventDAO();
        initComponents();
        loadParticipants();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Participant Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Participant");
        JButton btnDelete = new JButton("Delete Participant");
        JButton btnRegister = new JButton("Register to Event");
        
        btnAdd.addActionListener(e -> showAddDialog());
        btnDelete.addActionListener(e -> deleteParticipant());
        btnRegister.addActionListener(e -> registerToEvent());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnDelete);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        String[] columns = {"ID", "Name", "Email", "Phone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        participantTable = new JTable(tableModel);
        participantTable.setRowHeight(30);
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(participantTable), BorderLayout.CENTER);
    }

    private void loadParticipants() {
        tableModel.setRowCount(0);
        try {
            List<Participant> list = participantDAO.getAllParticipants();
            for (Participant p : list) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getEmail(), p.getPhone()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddDialog() {
        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPhone = new JTextField();
        
        Object[] message = {
            "Name:", txtName,
            "Email:", txtEmail,
            "Phone:", txtPhone
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Add Participant", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Participant p = new Participant(0, txtName.getText(), txtEmail.getText(), txtPhone.getText());
                participantDAO.addParticipant(p);
                loadParticipants();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding participant", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteParticipant() {
        int row = participantTable.getSelectedRow();
        if (row >= 0) {
            int id = (int) tableModel.getValueAt(row, 0);
            try {
                participantDAO.deleteParticipant(id);
                loadParticipants();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a participant to delete");
        }
    }

    private void registerToEvent() {
        int row = participantTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a participant first");
            return;
        }
        
        int participantId = (int) tableModel.getValueAt(row, 0);
        
        try {
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No events available to register.");
                return;
            }
            
            JComboBox<String> combo = new JComboBox<>();
            for (Event e : events) {
                combo.addItem(e.getId() + " - " + e.getName());
            }
            
            int option = JOptionPane.showConfirmDialog(this, combo, "Select Event", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String selected = (String) combo.getSelectedItem();
                int eventId = Integer.parseInt(selected.split(" - ")[0]);
                participantDAO.registerParticipantToEvent(participantId, eventId);
                JOptionPane.showMessageDialog(this, "Participant successfully registered to Event!");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error during registration: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
