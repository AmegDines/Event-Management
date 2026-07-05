package com.eventmanager.model;

public abstract class Event {
    private int id;
    private String name;
    private String category;
    private String venue;
    private String description;
    private String status; // e.g., Upcoming, Ongoing, Completed

    public Event(int id, String name, String category, String venue, String description, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.venue = venue;
        this.description = description;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public abstract String getEventType();
}
