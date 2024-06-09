package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RoomRequest {
    private static final String BASE_URL = "http://localhost:3000";

    public List<Room> getAvailableRooms(int singleBeds, int doubleBeds, String startDate, String endDate){
        List<Room> rooms = new ArrayList<>();
        String url = String.format("%s/available-rooms?single_beds=%d&double_beds=%d&start_date=%s&end_date=%s", BASE_URL, singleBeds, doubleBeds, startDate, endDate);

        try {
            URL urlUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlUrl.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            ArrayNode jsonArray = (ArrayNode) mapper.readTree(content.toString());

            for (JsonNode jsonNode : jsonArray) {
                Room room = new Room(
                        jsonNode.get("room_id").asInt(),
                        jsonNode.get("number").asText(),
                        jsonNode.get("type").asText(),
                        jsonNode.get("num_of_single_beds").asInt(),
                        jsonNode.get("num_of_double_beds").asInt(),
                        jsonNode.get("is_balcony").asText(),
                        Double.parseDouble(jsonNode.get("price_for_night").asText())
                );
                rooms.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }
}
