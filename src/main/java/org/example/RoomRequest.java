package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomRequest {
    private static final String BASE_URL = "http://localhost:3000";

    public List<Room> getAvailableRooms(int singleBeds, int doubleBeds, String startDate, String endDate) {
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

    public void bookRoom(int roomId, Date startDate, Date endDate, String firstName, String lastName, String email, String phone, String city, String country, String postalCode, String street, String number) throws Exception {
        String url = BASE_URL + "/reservations";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);

        String requestBody = String.format("{\"first_name\":\"%s\",\"last_name\":\"%s\",\"email\":\"%s\",\"phone_number\":\"%s\",\"address\":{\"city\":\"%s\",\"country\":\"%s\",\"postal_code\":\"%s\",\"street\":\"%s\",\"number\":\"%s\"},\"reservation\":{\"room_id\":%d,\"begin_of_stay_date\":\"%s\",\"end_of_stay_date\":\"%s\",\"status\":\"oczekujaca\"}}",
                firstName, lastName, email, phone, city, country, postalCode, street, number, roomId, startDateStr, endDateStr);

        System.out.println("Request Body: " + requestBody);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = connection.getResponseCode();
        if (code != HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                throw new Exception("Failed to book room: HTTP error code " + code + ", response: " + response.toString());
            }
        }
    }

    public List<Reservation> searchReservations(String query) {
        List<Reservation> reservations = new ArrayList<>();
        String url = String.format("%s/search-reservations?query=%s", BASE_URL, query);

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
                Reservation reservation = new Reservation(
                        jsonNode.get("reservation_id").asInt(),
                        jsonNode.get("first_name").asText(),
                        jsonNode.get("last_name").asText(),
                        jsonNode.get("room_number").asText(),
                        jsonNode.get("status").asText()
                );
                reservations.add(reservation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public String getPaymentStatus(int reservationId) {
        String status = "";
        String url = String.format("%s/reservations/%d", BASE_URL, reservationId);

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
            JsonNode jsonNode = mapper.readTree(content.toString());
            status = jsonNode.get("status").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public void updatePaymentStatus(int reservationId, String status) {
        String url = String.format("%s/reservations/%d", BASE_URL, reservationId);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String requestBody = String.format("{\"status\":\"%s\"}", status);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    throw new Exception("Failed to update payment status: HTTP error code " + code + ", response: " + response.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation(int reservationId) {
        String url = String.format("%s/reservations/%d", BASE_URL, reservationId);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("DELETE");

            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    throw new Exception("Failed to delete reservation: HTTP error code " + code + ", response: " + response.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
