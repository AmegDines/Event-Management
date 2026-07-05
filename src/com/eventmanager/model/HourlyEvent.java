package com.eventmanager.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class HourlyEvent extends Event {
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public HourlyEvent(int id, String name, String category, String venue, String description, String status, LocalDate eventDate, LocalTime startTime, LocalTime endTime) {
        super(id, name, category, venue, description, status);
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    @Override
    public String getEventType() {
        return "HOURLY";
    }
}
