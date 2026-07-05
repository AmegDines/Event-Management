package com.eventmanager.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    
    public MainFrame() {
        setTitle("Event Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Sidebar Navigation
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setBackground(new Color(45, 52, 54)); // Darker gray
        
        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnEvents = createSidebarButton("Events");
        JButton btnParticipants = createSidebarButton("Participants");
        JButton btnCalendar = createSidebarButton("Calendar");
        
        sidebar.add(btnDashboard);
        sidebar.add(btnEvents);
        sidebar.add(btnParticipants);
        sidebar.add(btnCalendar);
        
        // Main Content Area (CardLayout)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        
        mainContentPanel.add(new DashboardPanel(), "Dashboard");
        mainContentPanel.add(new EventManagementPanel(), "Events");
        mainContentPanel.add(new ParticipantManagementPanel(), "Participants");
        mainContentPanel.add(new CalendarPanel(), "Calendar");
        
        // Button actions
        btnDashboard.addActionListener(e -> cardLayout.show(mainContentPanel, "Dashboard"));
        btnEvents.addActionListener(e -> cardLayout.show(mainContentPanel, "Events"));
        btnParticipants.addActionListener(e -> cardLayout.show(mainContentPanel, "Participants"));
        btnCalendar.addActionListener(e -> cardLayout.show(mainContentPanel, "Calendar"));
        
        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }
    
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(99, 110, 114)); // Lighter gray
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return btn;
    }
}
