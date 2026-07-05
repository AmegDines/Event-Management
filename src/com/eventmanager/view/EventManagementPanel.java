package com.eventmanager.view;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.model.Event;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EventManagementPanel extends JPanel {
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private EventDAO eventDAO;
    private List<Event> currentEvents;

    public EventManagementPanel() {
        eventDAO = new EventDAO();
        initComponents();
        loadEvents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Event Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Event");
        JButton btnDelete = new JButton("Delete Event");
        
        btnAdd.setBackground(new Color(0, 122, 204));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> showAddEventDialog());

        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> deleteSelectedEvent());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        String[] columns = {"ID", "Name", "Category", "Venue", "Status", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        eventTable = new JTable(tableModel);
        eventTable.setRowHeight(30);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(eventTable), BorderLayout.CENTER);
    }

    private void loadEvents() {
        tableModel.setRowCount(0);
        try {
            currentEvents = eventDAO.getAllEvents();
            for (Event event : currentEvents) {
                tableModel.addRow(new Object[]{
                        event.getId(),
                        event.getName(),
                        event.getCategory(),
                        event.getVenue(),
                        event.getStatus(),
                        event.getEventType()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading events", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddEventDialog() {
        EventFormDialog dialog = new EventFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), eventDAO);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadEvents();
        }
    }

    private void deleteSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    eventDAO.deleteEvent(eventId);
                    loadEvents();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error deleting event", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an event to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
