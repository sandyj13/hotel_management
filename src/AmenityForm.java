import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class AmenityForm extends JFrame {
    private JPanel formPanel;
    private JComboBox<String> amenityNameField;
    private JTextField timeslotField;
    private JTextField dateField;
    private JComboBox<Integer> reservationIdField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTable amenityTable;
    private DefaultTableModel tableModel;

    private AmenityDAO amenityDAO;

    public AmenityForm() {
        setTitle("Amenity Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        amenityDAO = new AmenityDAO();

        formPanel.add(new JLabel("Amenity Name:"));
        amenityNameField = new JComboBox<>(new String[]{"Fitness Center", "Pool", "Spa", "Rooftop Deck"});
        formPanel.add(amenityNameField);

        formPanel.add(new JLabel("Timeslot (HH:MM:SS):"));
        timeslotField = new JTextField();
        formPanel.add(timeslotField);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Reservation ID:"));
        reservationIdField = new JComboBox<>();
        try {
            List<Integer> reservationIds = amenityDAO.getAllReservationIds();
            for (int reservationId : reservationIds) {
                reservationIdField.addItem(reservationId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        formPanel.add(reservationIdField);

        addButton = new JButton("Add Amenity Reservation");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String amenityName = (String) amenityNameField.getSelectedItem();
                    Time timeslot = validateAndParseTime(timeslotField.getText());
                    Date date = validateAndParseDate(dateField.getText());
                    int reservationId = (int) reservationIdField.getSelectedItem();

                    Amenity amenity = new Amenity(0, amenityName, timeslot, date, reservationId);
                    amenityDAO.addAmenity(amenity);

                    refreshAmenityList();
                    clearFields();
                } catch (SQLException | IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(AmenityForm.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        formPanel.add(addButton);

        deleteButton = new JButton("Delete Amenity Reservation");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = amenityTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int amenityId = (int) tableModel.getValueAt(selectedRow, 0);
                        amenityDAO.deleteAmenity(amenityId);
                        refreshAmenityList();
                    } else {
                        JOptionPane.showMessageDialog(AmenityForm.this, "Please select an amenity reservation to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AmenityForm.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        formPanel.add(deleteButton);

        updateButton = new JButton("Update Amenity Reservation");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = amenityTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int amenityId = (int) tableModel.getValueAt(selectedRow, 0);
                        String amenityName = (String) amenityNameField.getSelectedItem();
                        Time timeslot = validateAndParseTime(timeslotField.getText());
                        Date date = validateAndParseDate(dateField.getText());
                        int reservationId = (int) reservationIdField.getSelectedItem();

                        Amenity amenity = new Amenity(amenityId, amenityName, timeslot, date, reservationId);
                        amenityDAO.updateAmenity(amenity);

                        refreshAmenityList();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(AmenityForm.this, "Please select an amenity reservation to update.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException | IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(AmenityForm.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        formPanel.add(updateButton);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Amenity ID", "Amenity Name", "Timeslot", "Date", "Reservation ID"}, 0);
        amenityTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(amenityTable);
        add(scrollPane, BorderLayout.CENTER);

        setupTableSelectionListener();
        refreshAmenityList();
    }

    private void setupTableSelectionListener() {
        amenityTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && amenityTable.getSelectedRow() != -1) {
                    int selectedRow = amenityTable.getSelectedRow();
                    amenityNameField.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString());
                    timeslotField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    dateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    reservationIdField.setSelectedItem(Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString()));
                }
            }
        });
    }

    private void refreshAmenityList() {
        try {
            tableModel.setRowCount(0); // Clear existing rows
            List<Amenity> amenities = amenityDAO.getAllAmenities();
            for (Amenity amenity : amenities) {
                tableModel.addRow(new Object[]{
                        amenity.getAmenityId(),
                        amenity.getAmenityName(),
                        amenity.getTimeslot().toString(),
                        amenity.getDate().toString(),
                        amenity.getReservationId()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        amenityNameField.setSelectedIndex(0);
        timeslotField.setText("");
        dateField.setText("");
        reservationIdField.setSelectedIndex(0);
    }

    private Time validateAndParseTime(String timeStr) {
        try {
            return Time.valueOf(timeStr);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid timeslot format. Please use HH:MM:SS.");
        }
    }

    private Date validateAndParseDate(String dateStr) {
        try {
            return Date.valueOf(dateStr);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AmenityForm form = new AmenityForm();
            form.setVisible(true);
        });
    }
}
