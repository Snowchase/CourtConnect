package edu.Controller;

public class Controller {

    private SQliteLoginManager loginManager;
    private SQliteRegistrationManager registrationManager;
    private SQLiteJoinEventManager joinEventManager;

    public Controller() {
        DatabaseManager.initializeDatabase();
        loginManager = new SQliteLoginManager();
        registrationManager = new SQliteRegistrationManager();
        joinEventManager = new SQLiteJoinEventManager();
    }

    public int login(String username, String password) {
        if (loginManager.authenticateUser(username, password)) {
            return loginManager.getAthleteId(username, password);
        }
        return -1;
    }

    public boolean register(String username, String password) {
        return registrationManager.registerAthlete(username, password);
    }

    public String joinEvent(int athleteId, int eventId) {
        if (!joinEventManager.athleteExists(athleteId)) {
            return "ATHLETE_NOT_FOUND";
        }

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

    public Object[][] getAllEvents() {
        return joinEventManager.getAllEvents();
    }
}
