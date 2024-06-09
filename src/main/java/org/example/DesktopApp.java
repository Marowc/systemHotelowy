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
    private final JTextField singleBedsField;
    private final JTextField doubleBedsField;
    private final JTextArea resultArea;
    private final JDateChooser startDateChooser;
    private final JDateChooser endDateChooser;

    public DesktopApp() {
        setTitle("Room Availability Checker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
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

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
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

                resultArea.setText("");
                for (Room room : availableRooms) {
                    resultArea.append("Room ID: " + room.room_id + "\n");
                    resultArea.append("Number: " + room.number + "\n");
                    resultArea.append("Type: " + room.type + "\n");
                    resultArea.append("Single Beds: " + room.num_of_single_beds + "\n");
                    resultArea.append("Double Beds: " + room.num_of_double_beds + "\n");
                    resultArea.append("Balcony: " + room.is_balcony + "\n");
                    resultArea.append("Price per Night: " + room.price_for_night + "\n");
                    resultArea.append("\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(DesktopApp.this, "Invalid input or error fetching data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
