package com.eventmanager.view;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.model.DayBasedEvent;
import com.eventmanager.model.Event;
import com.eventmanager.model.HourlyEvent;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarPanel extends JPanel {
    private JPanel gridPanel;
    private JLabel lblMonth;
    private YearMonth currentMonth;
    private EventDAO eventDAO;

    public CalendarPanel() {
        eventDAO = new EventDAO();
        currentMonth = YearMonth.now();
        initComponents();
        renderCalendar();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnPrev = new JButton("<");
        JButton btnNext = new JButton(">");
        lblMonth = new JLabel("", SwingConstants.CENTER);
        lblMonth.setFont(new Font("Segoe UI", Font.BOLD, 20));

        btnPrev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            renderCalendar();
        });
        btnNext.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            renderCalendar();
        });

        topPanel.add(btnPrev);
        topPanel.add(lblMonth);
        topPanel.add(btnNext);

        gridPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        
        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    private void renderCalendar() {
        gridPanel.removeAll();
        lblMonth.setText(currentMonth.getMonth().name() + " " + currentMonth.getYear());

        // Fetch events for highlighting
        Set<Integer> eventDays = new HashSet<>();
        try {
            List<Event> events = eventDAO.getAllEvents();
            for (Event e : events) {
                if (e instanceof DayBasedEvent) {
                    LocalDate start = ((DayBasedEvent) e).getStartDate();
                    LocalDate end = ((DayBasedEvent) e).getEndDate();
                    // Just a simple highlight if it falls in the current month
                    if (start.getYear() == currentMonth.getYear() && start.getMonth() == currentMonth.getMonth()) {
                        eventDays.add(start.getDayOfMonth());
                    }
                } else if (e instanceof HourlyEvent) {
                    LocalDate date = ((HourlyEvent) e).getEventDate();
                    if (date.getYear() == currentMonth.getYear() && date.getMonth() == currentMonth.getMonth()) {
                        eventDays.add(date.getDayOfMonth());
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel d = new JLabel(day, SwingConstants.CENTER);
            d.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gridPanel.add(d);
        }

        LocalDate firstDay = currentMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Sunday = 0

        for (int i = 0; i < dayOfWeek; i++) {
            gridPanel.add(new JLabel("")); // Empty padding
        }

        int daysInMonth = currentMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            JPanel dayCell = new JPanel(new BorderLayout());
            dayCell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            dayCell.setBackground(Color.WHITE);
            
            JLabel lblDay = new JLabel(String.valueOf(i));
            lblDay.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
            dayCell.add(lblDay, BorderLayout.NORTH);
            
            if (currentMonth.equals(YearMonth.now()) && i == LocalDate.now().getDayOfMonth()) {
                dayCell.setBackground(new Color(173, 216, 230)); // Highlight today (Light Blue)
            }
            
            if (eventDays.contains(i)) {
                JLabel eventIndicator = new JLabel("★ Event", SwingConstants.CENTER);
                eventIndicator.setForeground(new Color(255, 140, 0)); // Orange
                dayCell.add(eventIndicator, BorderLayout.CENTER);
            }
            
            gridPanel.add(dayCell);
        }

        revalidate();
        repaint();
    }
}
