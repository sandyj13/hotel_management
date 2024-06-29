import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomServiceDAO {
    private Connection connection;

    public RoomServiceDAO() {
        try {
            connection = dbconnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addRoomService(RoomService roomService) throws SQLException {
        String sql = "INSERT INTO room_services (service_name, price, room_number) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roomService.getServiceName());
            statement.setDouble(2, roomService.getPrice());
            statement.setInt(3, roomService.getRoomNumber());
            statement.executeUpdate();
        }
    }

    public void deleteRoomService(int serviceId) throws SQLException {
        String sql = "DELETE FROM room_services WHERE service_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, serviceId);
            statement.executeUpdate();
        }
    }

    public List<Integer> getAllRoomNumbers() throws SQLException {
        List<Integer> roomNumbers = new ArrayList<>();
        String sql = "SELECT room_number FROM rooms";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                roomNumbers.add(resultSet.getInt("room_number"));
            }
        }
        return roomNumbers;
    }

    public List<RoomService> getAllRoomServices() throws SQLException {
        List<RoomService> roomServices = new ArrayList<>();
        String sql = "SELECT service_id, service_name, price, room_number FROM room_services";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int serviceId = resultSet.getInt("service_id");
                String serviceName = resultSet.getString("service_name");
                double price = resultSet.getDouble("price");
                int roomNumber = resultSet.getInt("room_number");
                roomServices.add(new RoomService(serviceId, serviceName, price, roomNumber));
            }
        }
        return roomServices;
    }

    public void updateRoomService(RoomService roomService) throws SQLException {
        String sql = "UPDATE room_services SET service_name = ?, price = ?, room_number = ? WHERE service_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roomService.getServiceName());
            statement.setDouble(2, roomService.getPrice());
            statement.setInt(3, roomService.getRoomNumber());
            statement.setInt(4, roomService.getServiceId());
            statement.executeUpdate();
        }
    }
}
