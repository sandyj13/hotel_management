import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AmenityDAO {
    private Connection connection;

    public AmenityDAO() {
        try {
            connection = dbconnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addAmenity(Amenity amenity) throws SQLException {
        String sql = "INSERT INTO hotel_amenities (amenity_name, timeslot, date, reservation_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, amenity.getAmenityName());
            statement.setTime(2, amenity.getTimeslot());
            statement.setDate(3, amenity.getDate());
            statement.setInt(4, amenity.getReservationId());
            statement.executeUpdate();
        }
    }

    public void deleteAmenity(int amenityId) throws SQLException {
        String sql = "DELETE FROM hotel_amenities WHERE amenity_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, amenityId);
            statement.executeUpdate();
        }
    }

    public void updateAmenity(Amenity amenity) throws SQLException {
        String sql = "UPDATE hotel_amenities SET amenity_name = ?, timeslot = ?, date = ?, reservation_id = ? WHERE amenity_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, amenity.getAmenityName());
            statement.setTime(2, amenity.getTimeslot());
            statement.setDate(3, amenity.getDate());
            statement.setInt(4, amenity.getReservationId());
            statement.setInt(5, amenity.getAmenityId());
            statement.executeUpdate();
        }
    }

    public List<Amenity> getAllAmenities() throws SQLException {
        List<Amenity> amenities = new ArrayList<>();
        String sql = "SELECT * FROM hotel_amenities";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int amenityId = resultSet.getInt("amenity_id");
                String amenityName = resultSet.getString("amenity_name");
                Time timeslot = resultSet.getTime("timeslot");
                Date date = resultSet.getDate("date");
                int reservationId = resultSet.getInt("reservation_id");
                Amenity amenity = new Amenity(amenityId, amenityName, timeslot, date, reservationId);
                amenities.add(amenity);
            }
        }
        return amenities;
    }

    public List<Integer> getAllReservationIds() throws SQLException {
        List<Integer> reservationIds = new ArrayList<>();
        String sql = "SELECT reservation_id FROM reservations WHERE reservation_for='amenities'"; // Adjust the table name if necessary
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                reservationIds.add(resultSet.getInt("reservation_id"));
            }
        }
        return reservationIds;
    }


    public Amenity getAmenityById(int amenityId) throws SQLException {
        String sql = "SELECT * FROM hotel_amenities WHERE amenity_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, amenityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String amenityName = resultSet.getString("amenity_name");
                    Time timeslot = resultSet.getTime("timeslot");
                    Date date = resultSet.getDate("date");
                    int reservationId = resultSet.getInt("reservation_id");
                    return new Amenity(amenityId, amenityName, timeslot, date, reservationId);
                }
            }
        }
        return null;
    }
}
