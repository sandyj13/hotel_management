import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HousekeepingDAO {
    private Connection connection;

    public HousekeepingDAO() {
        try {
            connection = dbconnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Integer> getAllStaffIds() throws SQLException {
        List<Integer> staffIds = new ArrayList<>();
        String sql = "SELECT staff_id FROM staff";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                staffIds.add(resultSet.getInt("staff_id"));
            }
        }
        return staffIds;
    }

    public boolean isRoomExists(int roomNumber) throws SQLException {
        String sql = "SELECT 1 FROM rooms WHERE room_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void addHousekeeping(Housekeeping housekeeping) throws SQLException {
        String sql = "INSERT INTO housekeeping (staff_id, room_number, task, completion_status, date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, housekeeping.getStaffId());
            statement.setInt(2, housekeeping.getRoomNumber());
            statement.setString(3, housekeeping.getTask());
            statement.setString(4, housekeeping.isCompletionStatus() ? "Yes" : "No");
            statement.setDate(5, new java.sql.Date(housekeeping.getDate().getTime()));
            statement.executeUpdate();
        }
    }

    public void updateHousekeeping(Housekeeping housekeeping) throws SQLException {
        String sql = "UPDATE housekeeping SET staff_id = ?, room_number = ?, task = ?, completion_status = ?, date = ? " +
                "WHERE housekeeping_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, housekeeping.getStaffId());
            statement.setInt(2, housekeeping.getRoomNumber());
            statement.setString(3, housekeeping.getTask());
            statement.setString(4, housekeeping.isCompletionStatus() ? "Yes" : "No");
            statement.setDate(5, new java.sql.Date(housekeeping.getDate().getTime()));
            statement.setInt(6, housekeeping.getHousekeepingId());
            statement.executeUpdate();
        }
    }

    public void deleteHousekeeping(int housekeepingId) throws SQLException {
        String sql = "DELETE FROM housekeeping WHERE housekeeping_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, housekeepingId);
            statement.executeUpdate();
        }
    }

    public List<Housekeeping> getHousekeepingList() throws SQLException {
        List<Housekeeping> housekeepingList = new ArrayList<>();
        String sql = "SELECT * FROM housekeeping";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int housekeepingId = resultSet.getInt("housekeeping_id");
                int staffId = resultSet.getInt("staff_id");
                int roomNumber = resultSet.getInt("room_number");
                String task = resultSet.getString("task");
                String completionStatusStr = resultSet.getString("completion_status");
                boolean completionStatus = completionStatusStr.equals("Yes");
                Date date = resultSet.getDate("date");
                Housekeeping housekeeping = new Housekeeping(housekeepingId, staffId, roomNumber, task, completionStatus, date);
                housekeepingList.add(housekeeping);
            }
        }
        return housekeepingList;
    }
}
