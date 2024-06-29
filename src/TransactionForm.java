import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class TransactionForm extends JFrame {
    private JTextField guestIdField;
    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> typeComboBox;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private TransactionsDAO transactionDAO;

    public TransactionForm() {
        setTitle("Transaction Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        transactionDAO = new TransactionsDAO();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel guestIdLabel = new JLabel("Guest ID:");
        guestIdField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();
        JLabel dateLabel = new JLabel("Date (yyyy-mm-dd):");
        dateField = new JTextField();
        JLabel typeLabel = new JLabel("Type:");
        String[] types = {"Cash", "Card", "UPI", "Reward Points"};
        typeComboBox = new JComboBox<>(types);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTransaction();
            }
        });

        inputPanel.add(guestIdLabel);
        inputPanel.add(guestIdField);
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(typeLabel);
        inputPanel.add(typeComboBox);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        tableModel = new DefaultTableModel(new Object[]{"Transaction ID", "Guest ID", "Amount", "Date", "Type"}, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(transactionTable);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        loadTransactionData();
    }

    private void loadTransactionData() {
        try {
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            tableModel.setRowCount(0); // Clear existing data
            for (Transaction transaction : transactions) {
                tableModel.addRow(new Object[]{
                        transaction.getTransactionId(), transaction.getGuestId(),
                        transaction.getAmount(), transaction.getDate(), transaction.getType()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading transaction data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTransaction() {
        int guestId = Integer.parseInt(guestIdField.getText());
        double amount = Double.parseDouble(amountField.getText());
        String date = dateField.getText();
        String type = (String) typeComboBox.getSelectedItem();

        try {
            transactionDAO.addTransaction(new Transaction(guestId, amount, date, type));
            loadTransactionData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding transaction", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow != -1) {
            int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                transactionDAO.deleteTransaction(transactionId);
                loadTransactionData(); // Refresh the table
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting transaction", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransactionForm().setVisible(true));
    }
}
