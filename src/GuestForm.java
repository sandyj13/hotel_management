import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class GuestForm extends JFrame {
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTable guestTable;
    private DefaultTableModel tableModel;
    private GuestDAO guestDAO;
    private int selectedGuestId;

    public GuestForm() {
        setTitle("Guest Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        guestDAO = new GuestDAO();

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10)); // Increased row count to make room for the update button

        JLabel nameLabel = new JLabel("First Name:");
        nameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGuest();
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteGuest();
            }
        });

        JButton updateButton = new JButton("Update"); // Add the update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGuest();
            }
        });

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(lastNameLabel);
        inputPanel.add(lastNameField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        inputPanel.add(addressLabel);
        inputPanel.add(addressField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(updateButton); // Add the update button

        tableModel = new DefaultTableModel(new Object[]{"Guest ID", "First Name", "Last Name", "Email", "Phone", "Address"}, 0);
        guestTable = new JTable(tableModel);
        guestTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = guestTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < guestTable.getRowCount()) {
                    selectedGuestId = (int) tableModel.getValueAt(row, 0);
                    nameField.setText((String) tableModel.getValueAt(row, 1));
                    lastNameField.setText((String) tableModel.getValueAt(row, 2));
                    emailField.setText((String) tableModel.getValueAt(row, 3));
                    phoneField.setText((String) tableModel.getValueAt(row, 4));
                    addressField.setText((String) tableModel.getValueAt(row, 5));
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(guestTable);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        loadGuestData();
    }

    private void loadGuestData() {
        try {
            List<Guest> guests = guestDAO.getAllGuests();
            tableModel.setRowCount(0); // Clear existing data
            for (Guest guest : guests) {
                tableModel.addRow(new Object[]{
                        guest.getGuestId(), guest.getFirstName(), guest.getLastName(),
                        guest.getEmail(), guest.getPhone(), guest.getAddress()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading guest data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addGuest() {
        String firstName = nameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
            Guest guest = new Guest();
            guest.setFirstName(firstName);
            guest.setLastName(lastName);
            guest.setEmail(email);
            guest.setPhone(phone);
            guest.setAddress(address);

            try {
                guestDAO.addGuest(guest);
                loadGuestData(); // Refresh the table
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding guest", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateGuest() {
        String firstName = nameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
            Guest guest = new Guest();
            guest.setGuestId(selectedGuestId); // Use the selected guest id
            guest.setFirstName(firstName);
            guest.setLastName(lastName);
            guest.setEmail(email);
            guest.setPhone(phone);
            guest.setAddress(address);

            try {
                guestDAO.updateGuest(guest);
                loadGuestData(); // Refresh the table
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating guest", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteGuest() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow != -1) {
            int guestId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                guestDAO.deleteGuest(guestId);
                loadGuestData(); // Refresh the table
                clearFields(); // Clear the text fields after deleting
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting guest", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a guest to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void clearFields() {
        nameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GuestForm().setVisible(true));
    }
}
