import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class ReservationForm extends JFrame {
    private JTextField reservationIdField;
    private JComboBox<Integer> guestIdField;
    private JTextField totalCostField;
    private JComboBox<String> reservationForField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private int selectedReservationId;

    public ReservationForm() {
        setTitle("Reservation Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reservationDAO = new ReservationDAO();
        guestDAO = new GuestDAO();

        formPanel.add(new JLabel("Reservation ID:"));
        reservationIdField = new JTextField();
        reservationIdField.setEditable(false); // Make the Reservation ID field read-only
        formPanel.add(reservationIdField);

        formPanel.add(new JLabel("Guest ID:"));
        guestIdField = new JComboBox<>();
        try {
            List<Integer> guestIds = guestDAO.getAllGuestIds();
            for (int guestId : guestIds) {
                guestIdField.addItem(guestId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        formPanel.add(guestIdField);

        formPanel.add(new JLabel("Total Cost:"));
        totalCostField = new JTextField();
        formPanel.add(totalCostField);

        formPanel.add(new JLabel("Reservation For:"));
        String[] reservationTypes = {"Amenities", "Rooms", "Restaurant"};
        reservationForField = new JComboBox<>(reservationTypes);
        formPanel.add(reservationForField);

        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addReservation();
            }
        });
        formPanel.add(addButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteReservation();
            }
        });
        formPanel.add(deleteButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateReservation();
            }
        });
        formPanel.add(updateButton);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Reservation ID", "Guest ID", "Total Cost", "Reservation For"}, 0);
        reservationTable = new JTable(tableModel);
        reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = reservationTable.getSelectedRow();
                    if (row != -1) {
                        selectedReservationId = (int) tableModel.getValueAt(row, 0);
                        reservationIdField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                        guestIdField.setSelectedItem(tableModel.getValueAt(row, 1));
                        totalCostField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                        reservationForField.setSelectedItem(tableModel.getValueAt(row, 3));
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(reservationTable);
        add(tableScrollPane, BorderLayout.CENTER);

        refreshReservationList();
    }

    private void addReservation() {
        try {
            int guestId = (int) guestIdField.getSelectedItem();
            double totalCost = Double.parseDouble(totalCostField.getText());
            String reservationFor = (String) reservationForField.getSelectedItem();

            Reservation reservation = new Reservation(guestId, totalCost, reservationFor);
            reservationDAO.addReservation(reservation);

            refreshReservationList();
            clearFields();
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReservation() {
        try {
            int reservationId = Integer.parseInt(reservationIdField.getText());
            int guestId = (int) guestIdField.getSelectedItem();
            double totalCost = Double.parseDouble(totalCostField.getText());
            String reservationFor = (String) reservationForField.getSelectedItem();

            Reservation reservation = new Reservation(reservationId, guestId, totalCost, reservationFor);
            reservationDAO.updateReservation(reservation);

            refreshReservationList();
            clearFields();
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow != -1) {
            int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                reservationDAO.deleteReservation(reservationId);
                refreshReservationList();
                clearFields(); // Clear the text fields after deleting
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshReservationList() {
        try {
            List<Reservation> reservations = reservationDAO.getAllReservations();
            tableModel.setRowCount(0); // Clear existing data
            for (Reservation reservation : reservations) {
                tableModel.addRow(new Object[]{
                        reservation.getReservationId(),
                        reservation.getGuestId(),
                        reservation.getTotalCost(),
                        reservation.getReservationFor()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        reservationIdField.setText("");
        guestIdField.setSelectedIndex(0);
        totalCostField.setText("");
        reservationForField.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservationForm().setVisible(true));
    }
}
