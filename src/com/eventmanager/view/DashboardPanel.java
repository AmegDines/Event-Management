package com.eventmanager.view;

import com.eventmanager.dao.EventDAO;
import com.eventmanager.model.DayBasedEvent;
import com.eventmanager.model.Event;
import com.eventmanager.model.HourlyEvent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardPanel extends JPanel {
    
    private EventDAO eventDAO;

    public DashboardPanel() {
        eventDAO = new EventDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        
        try {
            List<Event> events = eventDAO.getAllEvents();
            
            // Map for Categories
            Map<String, Integer> categoryCount = new HashMap<>();
            // Map for Monthly Events
            Map<Month, Integer> monthCount = new HashMap<>();
            
            for (Event e : events) {
                categoryCount.put(e.getCategory(), categoryCount.getOrDefault(e.getCategory(), 0) + 1);
                
                Month m = null;
                if (e instanceof DayBasedEvent) {
                    m = ((DayBasedEvent) e).getStartDate().getMonth();
                } else if (e instanceof HourlyEvent) {
                    m = ((HourlyEvent) e).getEventDate().getMonth();
                }
                
                if (m != null) {
                    monthCount.put(m, monthCount.getOrDefault(m, 0) + 1);
                }
            }
            
            for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
                pieDataset.setValue(entry.getKey(), entry.getValue());
            }
            
            for (Month m : Month.values()) {
                int count = monthCount.getOrDefault(m, 0);
                if (count > 0) {
                    barDataset.addValue(count, "Events", m.name().substring(0, 3));
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JFreeChart pieChart = ChartFactory.createPieChart("Events by Category", pieDataset, true, true, false);
        ChartPanel piePanel = new ChartPanel(pieChart);
        
        JFreeChart barChart = ChartFactory.createBarChart("Monthly Events", "Month", "Total", barDataset);
        ChartPanel barPanel = new ChartPanel(barChart);

        chartsPanel.add(piePanel);
        chartsPanel.add(barPanel);

        add(chartsPanel, BorderLayout.CENTER);
    }
}
