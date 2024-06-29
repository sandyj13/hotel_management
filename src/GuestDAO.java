import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {
    private static final String INSERT_QUERY = "INSERT INTO guests (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM guests";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM guests WHERE guest_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM guests WHERE guest_id = ?";
    private static final String SELECT_ALL_GUEST_IDS_QUERY = "SELECT guest_id FROM guests";

    private FeedbackDAO feedbackDAO;
    private RoomDAO roomDAO;
    private ReservationDAO reservationDAO;
    private TransactionsDAO transactionDAO;
    private AmenityDAO amenityDAO;

    public GuestDAO() {
        feedbackDAO = new FeedbackDAO();
        roomDAO = new RoomDAO();
        reservationDAO = new ReservationDAO();
        transactionDAO = new TransactionsDAO();
        amenityDAO = new AmenityDAO();
    }

    public void addGuest(Guest guest) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            stmt.setString(3, guest.getEmail());
            stmt.setString(4, guest.getPhone());
            stmt.setString(5, guest.getAddress());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding guest failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    guest.setGuestId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Adding guest failed, no ID obtained.");
                }
            }
        }
    }
    public void updateGuest(Guest guest) throws SQLException {
        String query = "UPDATE guests SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? WHERE guest_id = ?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            stmt.setString(3, guest.getEmail());
            stmt.setString(4, guest.getPhone());
            stmt.setString(5, guest.getAddress());
            stmt.setInt(6, guest.getGuestId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Updating guest failed, no rows affected.");
            }
        }
    }


    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int guestId = rs.getInt("guest_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                guests.add(new Guest(guestId, firstName, lastName, email, phone, address));
            }
        }
        return guests;
    }

    public static Guest getGuestById(int guestId) throws SQLException {
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_QUERY)) {
            stmt.setInt(1, guestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    return new Guest(guestId, firstName, lastName, email, phone, address);
                }
            }
        }
        return null; // Return null if guest not found
    }

    public void deleteGuest(int guestId) throws SQLException {
        try {
            feedbackDAO.deleteFeedbackByGuestId(guestId);
            // Call similar methods from other DAOs to delete related data
            roomDAO.deleteRoomsByGuestId(guestId);
            reservationDAO.deleteReservationsByGuestId(guestId);
            transactionDAO.deleteTransactionsByGuestId(guestId);

            try (Connection conn = dbconnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {
                stmt.setInt(1, guestId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error deleting guest and related data", e);
        }
    }

    public List<Integer> getAllGuestIds() throws SQLException {
        List<Integer> guestIds = new ArrayList<>();
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_GUEST_IDS_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                guestIds.add(rs.getInt("guest_id"));
            }
        }
        return guestIds;
    }
}
