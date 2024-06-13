package org.example;

public class Reservation {
    private int reservationId;
    private String firstName;
    private String lastName;
    private String roomNumber;
    private String status;

    public Reservation(int reservationId, String firstName, String lastName, String roomNumber, String status) {
        this.reservationId = reservationId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getStatus() {
        return status;
    }
}
