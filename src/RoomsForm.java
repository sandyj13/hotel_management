import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class RoomsForm extends JFrame {
    private JTextField roomNumberField;
    private JComboBox<String> roomTypeField;
    private JTextField capacityField;
    private JComboBox<Integer> guestIdField;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomDAO roomDAO;
    private int selectedRoomNumber;

    public RoomsForm() {
        setTitle("Rooms Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        roomDAO = new RoomDAO();

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // Adjusted grid layout

        inputPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        inputPanel.add(roomNumberField);

        inputPanel.add(new JLabel("Room Type:"));
        String[] roomTypes = {"Normal", "Deluxe", "Executive Suite", "Grand Luxury Suite"};
        roomTypeField = new JComboBox<>(roomTypes);
        inputPanel.add(roomTypeField);

        inputPanel.add(new JLabel("Capacity:"));
        capacityField = new JTextField();
        inputPanel.add(capacityField);

        inputPanel.add(new JLabel("Guest ID:"));
        guestIdField = new JComboBox<>();
        try {
            List<Integer> guestIds = new GuestDAO().getAllGuestIds();
            for (int guestId : guestIds) {
                guestIdField.addItem(guestId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        inputPanel.add(guestIdField);

        JButton addButton = new JButton("Add Room");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRoom();
            }
        });
        inputPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Room");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom();
            }
        });
        inputPanel.add(deleteButton);

        JButton updateButton = new JButton("Update Room");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoom();
            }
        });
        inputPanel.add(updateButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Room Number", "Room Type", "Capacity", "Guest ID"}, 0);
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Double-click event
                    int row = roomTable.getSelectedRow();
                    if (row != -1) {
                        selectedRoomNumber = (int) tableModel.getValueAt(row, 0);
                        roomNumberField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                        roomTypeField.setSelectedItem(tableModel.getValueAt(row, 1));
                        capacityField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                        guestIdField.setSelectedItem(tableModel.getValueAt(row, 3));
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(roomTable);
        add(tableScrollPane, BorderLayout.CENTER);

        refreshRoomList();
    }

    private void addRoom() {
        try {
            // Generate room number
            int roomNumber = roomDAO.getNextRoomNumber();
            String roomType = (String) roomTypeField.getSelectedItem();
            int capacity = Integer.parseInt(capacityField.getText());
            int guestId = (int) guestIdField.getSelectedItem();

            Room room = new Room(roomNumber, roomType, capacity, guestId);
            roomDAO.addRoom(room);
            refreshRoomList();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid capacity.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            String roomType = (String) roomTypeField.getSelectedItem();
            int capacity = Integer.parseInt(capacityField.getText());
            int guestId = (int) guestIdField.getSelectedItem();

            Room room = new Room(roomNumber, roomType, capacity, guestId);
            roomDAO.updateRoom(room);
            refreshRoomList();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid room number and capacity.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            int roomNumber = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                roomDAO.deleteRoom(roomNumber);
                refreshRoomList();
                clearFields(); // Clear the text fields after deleting
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshRoomList() {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            tableModel.setRowCount(0); // Clear existing data
            for (Room room : rooms) {
                tableModel.addRow(new Object[]{
                        room.getRoomNumber(), room.getRoomType(), room.getCapacity(), room.getGuestId()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        roomNumberField.setText("");
        roomTypeField.setSelectedIndex(0);
        capacityField.setText("");
        guestIdField.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RoomsForm().setVisible(true));
    }
}
