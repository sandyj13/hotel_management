import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FeedbackForm extends JFrame {
    private JPanel formPanel;
    private JTextField guestIdField;
    private JTextField feedbackDateField;
    private JTextField feedbackTextField;
    private JComboBox<Integer> ratingComboBox;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private FeedbackDAO feedbackDAO;

    public FeedbackForm() {
        setTitle("Feedback Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        feedbackDAO = new FeedbackDAO();

        formPanel.add(new JLabel("Guest ID:"));
        guestIdField = new JTextField();
        formPanel.add(guestIdField);

        formPanel.add(new JLabel("Feedback Date (yyyy-mm-dd):"));
        feedbackDateField = new JTextField();
        formPanel.add(feedbackDateField);

        formPanel.add(new JLabel("Feedback Text:"));
        feedbackTextField = new JTextField();
        formPanel.add(feedbackTextField);

        formPanel.add(new JLabel("Rating:"));
        Integer[] ratings = {1, 2, 3, 4, 5};
        ratingComboBox = new JComboBox<>(ratings);
        formPanel.add(ratingComboBox);

        JButton addButton = new JButton("Add Feedback");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFeedback();
            }
        });
        formPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Feedback");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFeedback();
            }
        });
        formPanel.add(deleteButton);

        tableModel = new DefaultTableModel(new Object[]{"Feedback ID", "Guest ID", "Feedback Text", "Rating", "Date"}, 0);
        feedbackTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(feedbackTable);

        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        loadFeedbackData();
    }

    private void loadFeedbackData() {
        try {
            List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
            tableModel.setRowCount(0); // Clear existing data
            for (Feedback feedback : feedbackList) {
                tableModel.addRow(new Object[]{
                        feedback.getFeedbackId(), feedback.getGuestId(),
                        feedback.getFeedbackText(), feedback.getRating(), feedback.getFeedbackDate()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading feedback data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFeedback() {
        try {
            int guestId = Integer.parseInt(guestIdField.getText());
            String feedbackDateStr = feedbackDateField.getText();
            java.sql.Date feedbackDate = validateAndParseDate(feedbackDateStr);
            if (feedbackDate == null) {
                JOptionPane.showMessageDialog(this, "Please enter a valid date in the format yyyy-mm-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String feedbackText = feedbackTextField.getText();
            int rating = (int) ratingComboBox.getSelectedItem();

            Feedback feedback = new Feedback(0, guestId, feedbackText, rating, feedbackDate);
            System.out.println("Attempting to add feedback: " + feedback);
            feedbackDAO.addFeedback(feedback);
            loadFeedbackData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Feedback added successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid input for Guest ID and Rating.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private java.sql.Date validateAndParseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try {
            java.util.Date parsed = format.parse(dateStr);
            return new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    private void deleteFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow != -1) {
            int feedbackId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                feedbackDAO.deleteFeedback(feedbackId);
                loadFeedbackData(); // Refresh the table
                JOptionPane.showMessageDialog(this, "Feedback deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting feedback", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select feedback to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        guestIdField.setText("");
        feedbackDateField.setText("");
        feedbackTextField.setText("");
        ratingComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeedbackForm().setVisible(true));
    }
}
