package org.example;

import java.util.Objects;

public class Room {
    public int room_id;
    public String number;
    public String type;
    public int num_of_single_beds;
    public int num_of_double_beds;
    public String is_balcony;
    public double price_for_night;

    public Room(int room_id, String number, String type, int num_of_single_beds, int num_of_double_beds, String is_balcony, double price_for_night){
        this.room_id = room_id;
        this.number = number;
        this.type = type;
        this.num_of_single_beds = num_of_single_beds;
        this.num_of_double_beds = num_of_double_beds;
        this.is_balcony = is_balcony;
        this.price_for_night = price_for_night;
    }

    @Override
    public String toString() {
        return "Room " + number + " (" + type + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return room_id == room.room_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(room_id);
    }
}
