package com.eventmanager.view;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.model.DayBasedEvent;
import com.eventmanager.model.HourlyEvent;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventFormDialog extends JDialog {
    private EventDAO eventDAO;
    private boolean saved = false;

    private JTextField txtName, txtCategory, txtVenue;
    private JTextArea txtDescription;
    private JComboBox<String> cbStatus, cbType;
    
    // Day Based Fields
    private DatePicker dpStartDate, dpEndDate;
    // Hourly Fields
    private DatePicker dpEventDate;
    private TimePicker tpStartTime, tpEndTime;

    private JPanel dynamicPanel;
    private CardLayout dynamicLayout;

    public EventFormDialog(JFrame parent, EventDAO eventDAO) {
        super(parent, "Add Event", true);
        this.eventDAO = eventDAO;
        setSize(400, 500);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Category:"));
        txtCategory = new JTextField();
        formPanel.add(txtCategory);

        formPanel.add(new JLabel("Venue:"));
        txtVenue = new JTextField();
        formPanel.add(txtVenue);

        formPanel.add(new JLabel("Status:"));
        cbStatus = new JComboBox<>(new String[]{"Upcoming", "Ongoing", "Completed"});
        formPanel.add(cbStatus);

        formPanel.add(new JLabel("Event Type:"));
        cbType = new JComboBox<>(new String[]{"DAY_BASED", "HOURLY"});
        formPanel.add(cbType);
        
        cbType.addActionListener(e -> updateDynamicFields());

        add(formPanel, BorderLayout.NORTH);

        dynamicLayout = new CardLayout();
        dynamicPanel = new JPanel(dynamicLayout);
        dynamicPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        // Day Based Panel
        JPanel dayBasedPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        dayBasedPanel.add(new JLabel("Start Date:"));
        dpStartDate = new DatePicker();
        dayBasedPanel.add(dpStartDate);
        dayBasedPanel.add(new JLabel("End Date:"));
        dpEndDate = new DatePicker();
        dayBasedPanel.add(dpEndDate);

        // Hourly Panel
        JPanel hourlyPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        hourlyPanel.add(new JLabel("Event Date:"));
        dpEventDate = new DatePicker();
        hourlyPanel.add(dpEventDate);
        hourlyPanel.add(new JLabel("Start Time:"));
        tpStartTime = new TimePicker();
        hourlyPanel.add(tpStartTime);
        hourlyPanel.add(new JLabel("End Time:"));
        tpEndTime = new TimePicker();
        hourlyPanel.add(tpEndTime);

        dynamicPanel.add(dayBasedPanel, "DAY_BASED");
        dynamicPanel.add(hourlyPanel, "HOURLY");

        add(dynamicPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        txtDescription = new JTextArea(3, 20);
        descPanel.add(new JScrollPane(txtDescription), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> saveEvent());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        bottomPanel.add(descPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateDynamicFields() {
        String type = (String) cbType.getSelectedItem();
        dynamicLayout.show(dynamicPanel, type);
    }

    private void saveEvent() {
        try {
            String type = (String) cbType.getSelectedItem();
            if ("DAY_BASED".equals(type)) {
                LocalDate sd = dpStartDate.getDate();
                LocalDate ed = dpEndDate.getDate();
                if (sd == null || ed == null) throw new IllegalArgumentException("Dates required");
                
                DayBasedEvent event = new DayBasedEvent(0, txtName.getText(), txtCategory.getText(), txtVenue.getText(), 
                        txtDescription.getText(), (String) cbStatus.getSelectedItem(), sd, ed);
                eventDAO.addEvent(event);
            } else {
                LocalDate ed = dpEventDate.getDate();
                LocalTime st = tpStartTime.getTime();
                LocalTime et = tpEndTime.getTime();
                if (ed == null || st == null || et == null) throw new IllegalArgumentException("Date/Time required");
                
                HourlyEvent event = new HourlyEvent(0, txtName.getText(), txtCategory.getText(), txtVenue.getText(), 
                        txtDescription.getText(), (String) cbStatus.getSelectedItem(), ed, st, et);
                eventDAO.addEvent(event);
            }
            saved = true;
            dispose();
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}
