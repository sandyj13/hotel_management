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

public class HousekeepingForm extends JFrame {
    private JPanel formPanel;
    private JComboBox<Integer> staffIdComboBox;
    private JTextField roomNumberField;
    private JTextField taskField;
    private JComboBox<String> completionStatusComboBox;
    private JTable housekeepingTable;
    private DefaultTableModel tableModel;
    private HousekeepingDAO housekeepingDAO;
    private int selectedHousekeepingId = -1;

    public HousekeepingForm() {
        setTitle("Housekeeping Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        housekeepingDAO = new HousekeepingDAO();

        formPanel.add(new JLabel("Staff ID:"));
        staffIdComboBox = new JComboBox<>();
        loadStaffIds();
        formPanel.add(staffIdComboBox);

        formPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Task:"));
        taskField = new JTextField();
        formPanel.add(taskField);

        formPanel.add(new JLabel("Completion Status:"));
        completionStatusComboBox = new JComboBox<>(new String[]{"Yes", "No", "On Process"});
        formPanel.add(completionStatusComboBox);

        JButton addButton = new JButton("Add Housekeeping");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHousekeeping();
            }
        });
        formPanel.add(addButton);

        JButton updateButton = new JButton("Update Housekeeping");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHousekeeping();
            }
        });
        formPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Housekeeping");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHousekeeping();
            }
        });
        formPanel.add(deleteButton);

        tableModel = new DefaultTableModel(new Object[]{"Housekeeping ID", "Staff ID", "Room Number", "Task", "Completion Status", "Date"}, 0);
        housekeepingTable = new JTable(tableModel);
        housekeepingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = housekeepingTable.getSelectedRow();
                if (row != -1) {
                    selectedHousekeepingId = (int) tableModel.getValueAt(row, 0);
                    populateFields(row);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(housekeepingTable);

        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        loadHousekeepingData();
    }

    private void loadStaffIds() {
        try {
            List<Integer> staffIds = housekeepingDAO.getAllStaffIds();
            for (Integer id : staffIds) {
                staffIdComboBox.addItem(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading staff IDs", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHousekeepingData() {
        try {
            List<Housekeeping> housekeepingList = housekeepingDAO.getHousekeepingList();
            tableModel.setRowCount(0); // Clear existing data
            for (Housekeeping housekeeping : housekeepingList) {
                tableModel.addRow(new Object[]{
                        housekeeping.getHousekeepingId(), housekeeping.getStaffId(),
                        housekeeping.getRoomNumber(), housekeeping.getTask(),
                        housekeeping.isCompletionStatus() ? "Yes" : "No",
                        housekeeping.getDate()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading housekeeping data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addHousekeeping() {
        try {
            int staffId = (int) staffIdComboBox.getSelectedItem();
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            if (!housekeepingDAO.isRoomExists(roomNumber)) {
                JOptionPane.showMessageDialog(this, "Room number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String task = taskField.getText();
            String completionStatusStr = (String) completionStatusComboBox.getSelectedItem();
            boolean completionStatus = completionStatusStr.equals("Yes");
            Date date = new Date(); // Assuming today's date
            Housekeeping housekeeping = new Housekeeping(0, staffId, roomNumber, task, completionStatus, date);
            housekeepingDAO.addHousekeeping(housekeeping);
            loadHousekeepingData();
            clearFields();
            JOptionPane.showMessageDialog(this, "Housekeeping added successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHousekeeping() {
        if (selectedHousekeepingId != -1) {
            try {
                int staffId = (int) staffIdComboBox.getSelectedItem();
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                if (!housekeepingDAO.isRoomExists(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Room number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String task = taskField.getText();
                String completionStatusStr = (String) completionStatusComboBox.getSelectedItem();
                boolean completionStatus = completionStatusStr.equals("Yes");
                Date date = new Date(); // Assuming today's date
                Housekeeping housekeeping = new Housekeeping(selectedHousekeepingId, staffId, roomNumber, task,
                        completionStatus, date);
                housekeepingDAO.updateHousekeeping(housekeeping);
                loadHousekeepingData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Housekeeping updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a housekeeping entry to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteHousekeeping() {
        int selectedRow = housekeepingTable.getSelectedRow();
        if (selectedRow != -1) {
            int housekeepingId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                housekeepingDAO.deleteHousekeeping(housekeepingId);
                loadHousekeepingData();
                JOptionPane.showMessageDialog(this, "Housekeeping deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting housekeeping", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select housekeeping to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateFields(int row) {
        staffIdComboBox.setSelectedItem(tableModel.getValueAt(row, 1));
        roomNumberField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        taskField.setText((String) tableModel.getValueAt(row, 3));
        String completionStatusStr = (String) tableModel.getValueAt(row, 4);
        completionStatusComboBox.setSelectedItem(completionStatusStr);
    }

    private void clearFields() {
        staffIdComboBox.setSelectedIndex(0);
        roomNumberField.setText("");
        taskField.setText("");
        completionStatusComboBox.setSelectedIndex(0);
        selectedHousekeepingId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HousekeepingForm().setVisible(true));
    }
}
