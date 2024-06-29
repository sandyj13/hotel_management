import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsDAO {
    private static Connection connection;

    public TransactionsDAO() {
        try {
            connection = dbconnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (guest_id, amount, transaction_date, transaction_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getGuestId());
            statement.setDouble(2, transaction.getAmount());
            statement.setString(3, transaction.getDate());
            statement.setString(4, transaction.getType());
            statement.executeUpdate();
        }
    }

    public void deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transactionId);
            statement.executeUpdate();
        }
    }

    public static void deleteTransactionsByGuestId(int guestId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE guest_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, guestId);
            statement.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getInt("transaction_id"),
                        resultSet.getInt("guest_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("transaction_date"),
                        resultSet.getString("transaction_type"));
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
