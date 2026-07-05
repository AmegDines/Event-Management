package com.eventmanager.model;

import java.time.LocalDate;

public class DayBasedEvent extends Event {
    private LocalDate startDate;
    private LocalDate endDate;

    public DayBasedEvent(int id, String name, String category, String venue, String description, String status, LocalDate startDate, LocalDate endDate) {
        super(id, name, category, venue, description, status);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    @Override
    public String getEventType() {
        return "DAY_BASED";
    }
}
