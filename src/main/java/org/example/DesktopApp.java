package org.example;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DesktopApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private CreateReservationPanel createReservationPanel;
    private StatusPanel statusPanel;

    public DesktopApp() {
        setTitle("Hotel Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels
        JPanel homePanel = createHomePanel();
        createReservationPanel = new CreateReservationPanel(this);
        statusPanel = new StatusPanel(this);

        // Add panels to main panel
        mainPanel.add(homePanel, "Home");
        mainPanel.add(createReservationPanel, "CreateReservation");
        mainPanel.add(statusPanel, "PaymentStatus");

        add(mainPanel);
        showHomePanel();
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton createReservationButton = new JButton("Create Reservation");
        createReservationButton.addActionListener(e -> showCreateReservationPanel());
        panel.add(createReservationButton);

        JButton checkPaymentStatusButton = new JButton("Check Payment Status");
        checkPaymentStatusButton.addActionListener(e -> showPaymentStatusPanel());
        panel.add(checkPaymentStatusButton);

        return panel;
    }

    public void showHomePanel() {
        cardLayout.show(mainPanel, "Home");
    }

    public void showCreateReservationPanel() {
        cardLayout.show(mainPanel, "CreateReservation");
    }

    public void showPaymentStatusPanel() {
        cardLayout.show(mainPanel, "PaymentStatus");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DesktopApp app = new DesktopApp();
            app.setVisible(true);
        });
    }
}

class CreateReservationPanel extends JPanel {
    private JTextField singleBedsField;
    private JTextField doubleBedsField;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private DefaultListModel<Room> listModel;
    private JList<Room> roomList;

    public CreateReservationPanel(DesktopApp app) {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Number of Single Beds:"));
        singleBedsField = new JTextField();
        inputPanel.add(singleBedsField);

        inputPanel.add(new JLabel("Number of Double Beds:"));
        doubleBedsField = new JTextField();
        inputPanel.add(doubleBedsField);

        inputPanel.add(new JLabel("Start Date:"));
        startDateChooser = new JDateChooser();
        inputPanel.add(startDateChooser);

        inputPanel.add(new JLabel("End Date:"));
        endDateChooser = new JDateChooser();
        inputPanel.add(endDateChooser);

        JButton checkButton = new JButton("Check Availability");
        checkButton.addActionListener(new CheckAvailabilityAction());
        inputPanel.add(checkButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.showHomePanel());
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        roomList = new JList<>(listModel);
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(roomList), BorderLayout.CENTER);

        JButton bookButton = new JButton("Book Room");
        bookButton.addActionListener(new BookRoomAction());
        add(bookButton, BorderLayout.SOUTH);
    }

    private class CheckAvailabilityAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int singleBeds = Integer.parseInt(singleBedsField.getText());
                int doubleBeds = Integer.parseInt(doubleBedsField.getText());
                Date startDate = startDateChooser.getDate();
                Date endDate = endDateChooser.getDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String startDateStr = dateFormat.format(startDate);
                String endDateStr = dateFormat.format(endDate);

                RoomRequest roomRequest = new RoomRequest();
                List<Room> availableRooms = roomRequest.getAvailableRooms(singleBeds, doubleBeds, startDateStr, endDateStr);

                listModel.clear();
                for (Room room : availableRooms) {
                    listModel.addElement(room);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(CreateReservationPanel.this, "Invalid input or error fetching data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class BookRoomAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Room selectedRoom = roomList.getSelectedValue();
            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(CreateReservationPanel.this, "Please select a room to book.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField cityField = new JTextField();
            JTextField countryField = new JTextField();
            JTextField postalCodeField = new JTextField();
            JTextField streetField = new JTextField();
            JTextField numberField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(10, 2));
            panel.add(new JLabel("First Name:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Last Name:"));
            panel.add(lastNameField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Phone Number:"));
            panel.add(phoneField);
            panel.add(new JLabel("City:"));
            panel.add(cityField);
            panel.add(new JLabel("Country:"));
            panel.add(countryField);
            panel.add(new JLabel("Postal Code:"));
            panel.add(postalCodeField);
            panel.add(new JLabel("Street:"));
            panel.add(streetField);
            panel.add(new JLabel("Number:"));
            panel.add(numberField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Enter Customer Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String city = cityField.getText();
                String country = countryField.getText();
                String postalCode = postalCodeField.getText();
                String street = streetField.getText();
                String number = numberField.getText();

                try {
                    RoomRequest roomRequest = new RoomRequest();
                    roomRequest.bookRoom(selectedRoom.room_id, startDateChooser.getDate(), endDateChooser.getDate(), firstName, lastName, email, phone, city, country, postalCode, street, number);
                    JOptionPane.showMessageDialog(CreateReservationPanel.this, "Room booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(CreateReservationPanel.this, "Error booking room.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}