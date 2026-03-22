package edu.Controller;

// File Name: Controller.java
// Group: 3
// Date: 3/21/2026
// Description: This class handles connections between UI and database(SQLite)

public class Controller {

    private final SQLiteJoinEventManager joinEventManager;

    public Controller() {
        this.joinEventManager = new SQLiteJoinEventManager();
    }

    public String joinEvent(int athleteId, int eventId) {
        if (!joinEventManager.eventExists(eventId)) {
            return "EVENT_NOT_FOUND";
        }

        if (joinEventManager.isEventFull(eventId)) {
            return "EVENT_FULL";
        }

        if (joinEventManager.isAlreadyJoined(athleteId, eventId)) {
            return "ALREADY_JOINED";
        }

        boolean success = joinEventManager.addParticipant(athleteId, eventId);

        if (success) {
            return "SUCCESS";
        } else {
            return "JOIN_FAILED";
        }
    }
}
