package edu.Controller;

public class Controller {

    private SQliteLoginManager loginManager;
    private SQliteRegistrationManager registrationManager;
    private SQLiteJoinEventManager joinEventManager;
    private SQLiteProfileManager profileManager;
    private SQLiteEventCreateManager eventCreateManager;

    public Controller() {
        DatabaseManager.initializeDatabase();
        loginManager = new SQliteLoginManager();
        registrationManager = new SQliteRegistrationManager();
        joinEventManager = new SQLiteJoinEventManager();
        profileManager = new SQLiteProfileManager();
        eventCreateManager = new SQLiteEventCreateManager();
    }

    public int login(String username, String password) {
        if (loginManager.authenticateUser(username, password)) {
            return loginManager.getAthleteId(username, password);
        }
        return -1;
    }

    public String getUserRole(String username, String password) {
        return loginManager.getUserRole(username, password);
    }

    public boolean register(String username, String password, String role) {
        return registrationManager.registerAthlete(username, password, role);
    }

    public String joinEvent(int athleteId, int eventId) {
        if (!joinEventManager.athleteExists(athleteId)) return "ATHLETE_NOT_FOUND";
        if (!joinEventManager.eventExists(eventId)) return "EVENT_NOT_FOUND";
        if (joinEventManager.isEventFull(eventId)) return "EVENT_FULL";
        if (joinEventManager.isAlreadyJoined(athleteId, eventId)) return "ALREADY_JOINED";

        boolean success = joinEventManager.addParticipant(athleteId, eventId);
        return success ? "SUCCESS" : "JOIN_FAILED";
    }

    public String leaveEvent(int athleteId, int eventId) {
        if (!joinEventManager.athleteExists(athleteId)) return "ATHLETE_NOT_FOUND";
        if (!joinEventManager.eventExists(eventId)) return "EVENT_NOT_FOUND";
        if (!joinEventManager.isAlreadyJoined(athleteId, eventId)) return "NOT_JOINED";

        boolean success = joinEventManager.leaveEvent(athleteId, eventId);
        return success ? "SUCCESS" : "LEAVE_FAILED";
    }

    public Object[][] getAllEvents() {
        return joinEventManager.getAllEvents();
    }

    public Object[][] getJoinedEventsForAthlete(int athleteId) {
        return joinEventManager.getJoinedEventsForAthlete(athleteId);
    }

    public Object[] getAthleteProfile(int athleteId) {
        return profileManager.getAthleteProfile(athleteId);
    }

    public boolean updateAthleteProfile(int athleteId, String name, int age, String skillLevel, String sex) {
        return profileManager.updateAthleteProfile(athleteId, name, age, skillLevel, sex);
    }

    public boolean createEvent(String eventName, String sport, String eventDate, String location,
                               String description, int maxPlayers, int createdBy) {
        return eventCreateManager.createEvent(eventName, sport, eventDate, location, description, maxPlayers, createdBy);
    }

    public String deleteEvent(int eventId, int organizerId) {
        if (!eventCreateManager.isEventCreatedByOrganizer(eventId, organizerId)) {
            return "NOT_OWNER";
        }

        boolean deleted = eventCreateManager.deleteEvent(eventId, organizerId);
        return deleted ? "SUCCESS" : "DELETE_FAILED";
    }
}