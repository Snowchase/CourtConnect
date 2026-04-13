public Object[][] getAllEvents() {
    java.util.List<Object[]> rows = new java.util.ArrayList<>();

    try {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT sportingid, sport, event_date, location FROM sporting_events ORDER BY sportingid"
        );

        while (rs.next()) {
            rows.add(new Object[]{
                    rs.getInt("sportingid"),
                    rs.getString("sport"),
                    rs.getString("event_date"),
                    rs.getString("location")
            });
        }

        rs.close();
        stmt.close();
        conn.close();

    } catch (SQLException e) {
        System.out.println("getAllEvents error: " + e.getMessage());
    }

    return rows.toArray(new Object[0][]);
}
