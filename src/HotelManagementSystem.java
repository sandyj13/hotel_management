import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotelManagementSystem {
    private JFrame frame;
    private JPanel mainPanel;

    public HotelManagementSystem() {
        frame = new JFrame("Hotel Management System");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new ImagePanel(); // Use custom panel that supports background image
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(10, 1, 10, 10)); // Panel to hold buttons
        buttonPanel.setOpaque(false); // Make the panel transparent

        // Create buttons and add them to the button panel
        buttonPanel.add(createButton("Guests"));
        buttonPanel.add(createButton("Rooms"));
        buttonPanel.add(createButton("Reservations"));
        buttonPanel.add(createButton("Amenities"));
        buttonPanel.add(createButton("Room Services"));
        buttonPanel.add(createButton("Housekeeping"));
        buttonPanel.add(createButton("Feedback"));
        buttonPanel.add(createButton("Staff"));
        buttonPanel.add(createButton("Transactions"));

        mainPanel.add(buttonPanel, BorderLayout.WEST); // Add button panel to the left side

        // Add a title label
        JLabel titleLabel = new JLabel("Hotel Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Method to create a button
    private JButton createButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Set custom font
        button.setForeground(Color.WHITE); // Set text color
        button.setBackground(new Color(53, 152, 219)); // Set background color
        button.setBorder(BorderFactory.createLineBorder(new Color(53, 152, 219), 2)); // Set border color and thickness
        button.setFocusPainted(false); // Disable focus painting
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openForm(buttonText);
            }
        });
        return button;
    }

    private void openForm(String entityName) {
        switch (entityName) {
            case "Guests":
                new GuestForm().setVisible(true);
                break;
            case "Rooms":
                new RoomsForm().setVisible(true);
                break;
            case "Reservations":
                new ReservationForm().setVisible(true);
                break;
            case "Amenities":
                new AmenityForm().setVisible(true);
                break;
            case "Room Services":
                new RoomServiceForm().setVisible(true);
                break;
            case "Housekeeping":
                new HousekeepingForm().setVisible(true);
                break;
            case "Feedback":
                new FeedbackForm().setVisible(true);
                break;
            case "Staff":
                new StaffForm().setVisible(true);
                break;
            case "Transactions":
                new TransactionForm().setVisible(true);
                break;
            default:
                break;
        }
    }

    public void setVisible(boolean b) {
    }

    // Custom panel class to support background image
    class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Set background color
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw background image
            ImageIcon backgroundImage = new ImageIcon("C:\\Users\\DELL\\Downloads\\pexels-boonkong-boonpeng-442952-1134176.jpg"); // Change to your image file
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);

            // Add a translucent overlay
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
