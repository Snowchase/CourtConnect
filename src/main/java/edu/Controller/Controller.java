package edu.Controller;

public class Controller {

    private SQliteLoginManager loginManager;
    private SQliteRegistrationManager registrationManager;
    private SQLiteJoinEventManager joinEventManager;
    //Additional Managers created
    private SQLiteCreateEventManager createEventManager;
    private SQLiteViewEventManager viewEventManager;
    private SQLiteCancelEventManager cancelEventManager;
    public Controller() {
        DatabaseManager.initializeDatabase();
        loginManager = new SQliteLoginManager();
        registrationManager = new SQliteRegistrationManager();
        joinEventManager = new SQLiteJoinEventManager();
        createEventManager = new SQLiteCreateEventManager();
        viewEventManager = new SQLiteViewEventManager();
        cancelEventManager = new SQLiteCancelEventManager();
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

    //Added methods for new managers
    //Create, View, Cancel Events
    public boolean createEvent(String sport, String date, String location, int maxParticipants) {
        //return createEventManager.createEvent(sport, date, location, maxParticipants);
        return false; // Placeholder return statement
    }
    public Object[][] viewEvents() {
        //return viewEventManager.getAllEvents();
        return new Object[0][0]; // Placeholder return statement
    }
    public boolean cancelEvent(int eventId) {
        //return cancelEventManager.cancelEvent(eventId);
        return false; // Placeholder return statement
    }

    public Object[][] getAllEvents() {
        return joinEventManager.getAllEvents();
    }
}
