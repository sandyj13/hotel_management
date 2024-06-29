import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private static final String INSERT_QUERY = "INSERT INTO reservations (guest_id, total_cost, reservation_for) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM reservations";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM reservations WHERE reservation_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM reservations WHERE reservation_id = ?";
    private static final String DELETE_BY_GUEST_ID_QUERY = "DELETE FROM reservations WHERE guest_id = ?";

    public void updateReservation(Reservation reservation) throws SQLException {
        String query = "UPDATE reservations SET guest_id = ?, total_cost = ?, reservation_for = ? WHERE reservation_id = ?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservation.getGuestId());
            stmt.setDouble(2, reservation.getTotalCost());
            stmt.setString(3, reservation.getReservationFor());
            stmt.setInt(4, reservation.getReservationId());
            stmt.executeUpdate();
        }
    }

    public void addReservation(Reservation reservation) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getGuestId());
            stmt.setDouble(2, reservation.getTotalCost());
            stmt.setString(3, reservation.getReservationFor());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding reservation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setReservationId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Adding reservation failed, no ID obtained.");
                }
            }
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                int guestId = rs.getInt("guest_id");
                double totalCost = rs.getDouble("total_cost");
                String reservationFor = rs.getString("reservation_for");
                reservations.add(new Reservation(reservationId, guestId, totalCost, reservationFor));
            }
        }
        return reservations;
    }

    public Reservation getReservationById(int reservationId) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_QUERY)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int guestId = rs.getInt("guest_id");
                    double totalCost = rs.getDouble("total_cost");
                    String reservationFor = rs.getString("reservation_for");
                    return new Reservation(reservationId, guestId, totalCost, reservationFor);
                }
            }
        }
        return null; // Return null if reservation not found
    }

    public void deleteReservation(int reservationId) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
        }
    }

    public static void deleteReservationsByGuestId(int guestId) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_GUEST_ID_QUERY)) {
            stmt.setInt(1, guestId);
            stmt.executeUpdate();
        }
    }
}
