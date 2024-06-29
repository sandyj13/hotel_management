import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection connection;

    public RoomDAO() {
        try {
            connection = dbconnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, room_type, capacity, guest_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setString(2, room.getRoomType());
            statement.setInt(3, room.getCapacity());
            statement.setInt(4, room.getGuestId());
            statement.executeUpdate();
        }
    }
    public int getNextRoomNumber() throws SQLException {
        int nextRoomNumber = 0;
        String query = "SELECT MAX(room_number) + 1 AS next_room_number FROM rooms";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                nextRoomNumber = resultSet.getInt("next_room_number");
            }
        }

        return nextRoomNumber;
    }

    public void deleteRoom(int roomNumber) throws SQLException {
        String sql = "DELETE FROM rooms WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomNumber);
            statement.executeUpdate();
        }
    }

    public void deleteRoomsByGuestId(int guestId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE guest_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, guestId);
            statement.executeUpdate();
        }
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT room_number, room_type, capacity, guest_id FROM rooms";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int roomNumber = resultSet.getInt("room_number");
                String roomType = resultSet.getString("room_type");
                int capacity = resultSet.getInt("capacity");
                int guestId = resultSet.getInt("guest_id");
                rooms.add(new Room(roomNumber, roomType, capacity, guestId));
            }
        }
        return rooms;
    }

    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_type = ?, capacity = ?, guest_id = ? WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room.getRoomType());
            statement.setInt(2, room.getCapacity());
            statement.setInt(3, room.getGuestId());
            statement.setInt(4, room.getRoomNumber());
            statement.executeUpdate();
        }
    }
}
