package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class StatusPanel extends JPanel {
    private JTextField searchField;
    private JTextArea resultArea;
    private JComboBox<String> statusComboBox;
    private RoomRequest roomRequest;
    private Reservation selectedReservation;

    public StatusPanel(DesktopApp app) {
        setLayout(new BorderLayout());

        roomRequest = new RoomRequest();

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Search by Name or Room Number:"));
        searchField = new JTextField();
        inputPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchReservationAction());
        inputPanel.add(searchButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.showHomePanel());
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new GridLayout(2, 2));
        statusPanel.add(new JLabel("Change Status:"));
        statusComboBox = new JComboBox<>(new String[]{"potwierdzona - oplacona", "oczekujaca", "anulowana"});
        statusPanel.add(statusComboBox);

        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.addActionListener(new UpdatePaymentStatusAction());
        statusPanel.add(updateStatusButton);

        JButton deleteReservationButton = new JButton("Delete Reservation");
        deleteReservationButton.addActionListener(new DeleteReservationAction());
        statusPanel.add(deleteReservationButton);

        add(statusPanel, BorderLayout.SOUTH);
    }

    private class SearchReservationAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchQuery = searchField.getText();
            List<Reservation> reservations = roomRequest.searchReservations(searchQuery);
            resultArea.setText("");
            if (reservations.isEmpty()) {
                resultArea.setText("No reservations found.");
            } else {
                for (Reservation reservation : reservations) {
                    resultArea.append("Reservation ID: " + reservation.getReservationId() + "\n");
                    resultArea.append("Name: " + reservation.getFirstName() + " " + reservation.getLastName() + "\n");
                    resultArea.append("Room Number: " + reservation.getRoomNumber() + "\n");
                    resultArea.append("Status: " + reservation.getStatus() + "\n\n");
                    selectedReservation = reservation;
                }
            }
        }
    }

    private class UpdatePaymentStatusAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedReservation == null) {
                JOptionPane.showMessageDialog(StatusPanel.this, "Please search and select a reservation first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            roomRequest.updatePaymentStatus(selectedReservation.getReservationId(), selectedStatus);
            resultArea.setText("Updated Payment Status to: " + selectedStatus);
        }
    }

    private class DeleteReservationAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedReservation == null) {
                JOptionPane.showMessageDialog(StatusPanel.this, "Please search and select a reservation first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            roomRequest.deleteReservation(selectedReservation.getReservationId());
            resultArea.setText("Reservation deleted.");
        }
    }
}
