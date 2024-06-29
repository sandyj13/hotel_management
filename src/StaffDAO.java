import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    private Connection connection;

    public StaffDAO() {
        try {
            connection = dbconnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff (first_name, last_name, position, email, phone, hire_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, staff.getFirstName());
            statement.setString(2, staff.getLastName());
            statement.setString(3, staff.getPosition());
            statement.setString(4, staff.getEmail());
            statement.setString(5, staff.getPhone());
            statement.setDate(6, new java.sql.Date(staff.getHireDate().getTime()));
            statement.executeUpdate();
        }
    }

    public void deleteStaff(int staffId) throws SQLException {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, staffId);
            statement.executeUpdate();
        }
    }

    public List<Staff> getStaffList() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT staff_id, first_name, last_name, position, email, phone, hire_date FROM staff";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int staffId = resultSet.getInt("staff_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String position = resultSet.getString("position");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                Date hireDate = resultSet.getDate("hire_date");
                staffList.add(new Staff(staffId, firstName, lastName, position, email, phone, hireDate));
            }
        }
        return staffList;
    }

    public void updateStaff(Staff staff) throws SQLException {
        String sql = "UPDATE staff SET first_name = ?, last_name = ?, position = ?, email = ?, phone = ?, hire_date = ? WHERE staff_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, staff.getFirstName());
            statement.setString(2, staff.getLastName());
            statement.setString(3, staff.getPosition());
            statement.setString(4, staff.getEmail());
            statement.setString(5, staff.getPhone());
            statement.setDate(6, new java.sql.Date(staff.getHireDate().getTime()));
            statement.setInt(7, staff.getStaffId());
            statement.executeUpdate();
        }
    }
}
