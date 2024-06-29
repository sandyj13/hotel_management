import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class StaffForm extends JFrame {
    private JPanel formPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField positionField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable staffTable;
    private DefaultTableModel tableModel;

    private StaffDAO staffDAO;
    private int selectedStaffId = -1;

    public StaffForm() {
        setTitle("Staff Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        formPanel = new JPanel(new GridLayout(7, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        staffDAO = new StaffDAO();

        formPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Position:"));
        positionField = new JTextField();
        formPanel.add(positionField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        addButton = new JButton("Add Staff Member");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaffMember();
            }
        });
        formPanel.add(addButton);

        updateButton = new JButton("Update Staff Member");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStaffMember();
            }
        });
        formPanel.add(updateButton);

        deleteButton = new JButton("Delete Staff Member");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStaffMember();
            }
        });
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);

        String[] columnNames = {"Staff ID", "First Name", "Last Name", "Position", "Email", "Phone", "Hire Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        staffTable = new JTable(tableModel);
        staffTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = staffTable.getSelectedRow();
                if (row != -1) {
                    selectedStaffId = (int) tableModel.getValueAt(row, 0);
                    firstNameField.setText((String) tableModel.getValueAt(row, 1));
                    lastNameField.setText((String) tableModel.getValueAt(row, 2));
                    positionField.setText((String) tableModel.getValueAt(row, 3));
                    emailField.setText((String) tableModel.getValueAt(row, 4));
                    phoneField.setText((String) tableModel.getValueAt(row, 5));
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(staffTable);
        add(tableScrollPane, BorderLayout.CENTER);

        loadStaffMembers();
    }

    private void addStaffMember() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String position = positionField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            Date hireDate = new Date();

            Staff staff = new Staff(0, firstName, lastName, position, email, phone, hireDate);
            staffDAO.addStaff(staff);

            JOptionPane.showMessageDialog(this, "Staff member added successfully.");
            clearFields();
            loadStaffMembers();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStaffMember() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow != -1) {
            int staffId = (int) staffTable.getValueAt(selectedRow, 0);
            try {
                staffDAO.deleteStaff(staffId);
                loadStaffMembers();
                JOptionPane.showMessageDialog(this, "Staff member deleted successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a staff member to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateStaffMember() {
        if (selectedStaffId != -1) {
            try {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String position = positionField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                Date hireDate = new Date();

                Staff staff = new Staff(selectedStaffId, firstName, lastName, position, email, phone, hireDate);
                staffDAO.updateStaff(staff);

                JOptionPane.showMessageDialog(this, "Staff member updated successfully.");
                clearFields();
                loadStaffMembers();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a staff member to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadStaffMembers() {
        try {
            List<Staff> staffList = staffDAO.getStaffList();
            tableModel.setRowCount(0); // Clear existing data
            for (Staff staff : staffList) {
                Object[] rowData = {
                        staff.getStaffId(), staff.getFirstName(), staff.getLastName(),
                        staff.getPosition(), staff.getEmail(), staff.getPhone(), staff.getHireDate()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setText("");
        emailField.setText("");
        phoneField.setText("");
        selectedStaffId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StaffForm().setVisible(true));
    }
}
