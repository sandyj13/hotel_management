import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class RoomServiceForm extends JFrame {
    private JPanel formPanel;
    private JComboBox<String> serviceNameField;
    private JComboBox<Integer> roomNumberField;
    private JTextField priceField;
    private JButton addButton;
    private JButton updateButton;
    private JTable serviceTable;
    private DefaultTableModel tableModel;

    private RoomServiceDAO roomServiceDAO;
    private int selectedServiceId;

    public RoomServiceForm() {
        setTitle("Room Service Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        roomServiceDAO = new RoomServiceDAO();

        formPanel.add(new JLabel("Service Name:"));
        String[] services = {"Food and Beverage Services", "Dining Services", "Transportation and Luggage Assistance", "Special Occasion Services"};
        serviceNameField = new JComboBox<>(services);
        formPanel.add(serviceNameField);

        formPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JComboBox<>();
        try {
            List<Integer> roomNumbers = roomServiceDAO.getAllRoomNumbers();
            for (int roomNumber : roomNumbers) {
                roomNumberField.addItem(roomNumber);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        addButton = new JButton("Add Service");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoomService();
            }
        });
        formPanel.add(addButton);

        updateButton = new JButton("Update Service");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoomService();
            }
        });
        formPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Service");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoomService();
            }
        });
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Service ID", "Service Name", "Price", "Room Number"}, 0);
        serviceTable = new JTable(tableModel);
        serviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = serviceTable.getSelectedRow();
                    if (row != -1) {
                        selectedServiceId = (int) tableModel.getValueAt(row, 0);
                        serviceNameField.setSelectedItem(tableModel.getValueAt(row, 1));
                        priceField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                        roomNumberField.setSelectedItem(tableModel.getValueAt(row, 3));
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(serviceTable);
        add(tableScrollPane, BorderLayout.CENTER);

        refreshServiceList();
    }

    private void addRoomService() {
        try {
            String serviceName = (String) serviceNameField.getSelectedItem();
            int roomNumber = (int) roomNumberField.getSelectedItem();
            double price = Double.parseDouble(priceField.getText());

            RoomService roomService = new RoomService(0, serviceName, price, roomNumber);
            roomServiceDAO.addRoomService(roomService);

            JOptionPane.showMessageDialog(this, "Room service added successfully.");
            refreshServiceList();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoomService() {
        int selectedRow = serviceTable.getSelectedRow();
        if (selectedRow != -1) {
            int serviceId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                roomServiceDAO.deleteRoomService(serviceId);
                refreshServiceList();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a service to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateRoomService() {
        try {
            String serviceName = (String) serviceNameField.getSelectedItem();
            int roomNumber = (int) roomNumberField.getSelectedItem();
            double price = Double.parseDouble(priceField.getText());

            RoomService roomService = new RoomService(selectedServiceId, serviceName, price, roomNumber);
            roomServiceDAO.updateRoomService(roomService);

            JOptionPane.showMessageDialog(this, "Room service updated successfully.");
            refreshServiceList();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshServiceList() {
        try {
            List<RoomService> roomServices = roomServiceDAO.getAllRoomServices();
            tableModel.setRowCount(0); // Clear existing data
            for (RoomService roomService : roomServices) {
                tableModel.addRow(new Object[]{
                        roomService.getServiceId(), roomService.getServiceName(), roomService.getPrice(), roomService.getRoomNumber()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        serviceNameField.setSelectedIndex(0);
        roomNumberField.setSelectedIndex(0);
        priceField.setText("");
        selectedServiceId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RoomServiceForm().setVisible(true));
    }
}
