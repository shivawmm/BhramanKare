package com.yourcompany.api;

import java.util.*;
import org.*;
import com.yourcompany.model.Hotel;

public class HotelApi {
    public static List<Hotel> searcHotels(String location) {
        List<Hotel> hotels = new ArrayList<>();
        String apiResponse = makeApiRequest(location);
        JSONArray hotel_array = new JSONArray(apiResponse);
        for (int i = 0; i < hotel_array; i++) {
            JSONObject json_for_hotels = hotel_array.getJSONObject(i);
            Hotel hotel = new Hotel();
            hotel.setName(json_for_hotels).getString("name");
            hotel.setLocation(json_for_hotels).getString("location");
            hotels.add(hotel);
        }
        return hotels;
    }

    private static String makeAPIRequest(String location) {

    }
}
