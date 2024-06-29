import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    private static final String INSERT_QUERY = "INSERT INTO feedback (guest_id, feedback_text, rating, feedback_date) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM feedback";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM feedback WHERE feedback_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM feedback WHERE feedback_id = ?";
    private static final String DELETE_BY_GUEST_ID_QUERY = "DELETE FROM feedback WHERE guest_id = ?";

    public void addFeedback(Feedback feedback) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, feedback.getGuestId());
            stmt.setString(2, feedback.getFeedbackText());
            stmt.setInt(3, feedback.getRating());
            stmt.setDate(4, feedback.getFeedbackDate());

            System.out.println("Executing SQL: " + stmt);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding feedback failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    feedback.setFeedbackId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Adding feedback failed, no ID obtained.");
                }
            }
        }
    }

    public List<Feedback> getAllFeedback() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int feedbackId = rs.getInt("feedback_id");
                int guestId = rs.getInt("guest_id");
                String feedbackText = rs.getString("feedback_text");
                int rating = rs.getInt("rating");
                java.sql.Date feedbackDate = rs.getDate("feedback_date");
                feedbackList.add(new Feedback(feedbackId, guestId, feedbackText, rating, feedbackDate));
            }
        }
        return feedbackList;
    }

    public Feedback getFeedbackById(int feedbackId) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_QUERY)) {
            stmt.setInt(1, feedbackId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int guestId = rs.getInt("guest_id");
                    String feedbackText = rs.getString("feedback_text");
                    int rating = rs.getInt("rating");
                    java.sql.Date feedbackDate = rs.getDate("feedback_date");
                    return new Feedback(feedbackId, guestId, feedbackText, rating, feedbackDate);
                }
            }
        }
        return null; // Return null if feedback not found
    }

    public void deleteFeedback(int feedbackId) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {
            stmt.setInt(1, feedbackId);
            stmt.executeUpdate();
        }
    }

    public static void deleteFeedbackByGuestId(int guestId) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_GUEST_ID_QUERY)) {
            stmt.setInt(1, guestId);
            stmt.executeUpdate();
        }
    }
}
