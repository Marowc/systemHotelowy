package org.example;

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
}
