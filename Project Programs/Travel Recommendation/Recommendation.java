import java.util.*;
import com.google.*;
import javax.servlet.*;
import java.io.*;
import java.net.*;
import java.rmi.ServerException;
import org.json.*;

public class Recommendation extends HttpServlet {
    private static final GeoApiContext context = new GeoApiContext().setApiKey();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String apiKey = "";
        String budget = request.getParameter("Budget");
        String place = request.getParameter("Place");
        String latitude = request.getParameter("Latitude");
        String longitude = request.getParameter("Longitude");
        String dist = request.getParameter("Distance_From_User");
        if (budget == null || budget.isEmpty()) {
            response.getStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWritera()
                    .write("Please give the Budget Idea, it seems you have not given the budget idea for travel");
            return;
        }
        if (place == null || place.isEmpty()) {
            response.getStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWritera()
                    .write("Please give the Travel Place Idea, it seems you have not given the place idea");
            return;
        }
        if (latitude == null || latitude.isEmpty()) {
            response.getStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWritera()
                    .write("Please give the your location Idea, it seems you have not given the your location");
            return;
        }
        if (longitude == null || longitude.isEmpty()) {
            response.getStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWritera()
                    .write("Please give the your location Idea, it seems you have not given the your location");
            return;
        }
        if (dist == null || state.isEmpty()) {
            response.getStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWritera()
                    .write("Please give the approximate distance from your home you are willing to travel, it seems you have not mentioned it");
            return;
        }
        List<Place> recommendations = getRecommendations(budget, place, latitude, longitude, dist);
        response.setContentType("applications/json");
        response.getWriter().write(new Gson().toJson(recommendations));
    }

    private static List<Place> getRecommendations(String budget, String place, String latitude, String longitude,
            String dist) {
        try {
            List<Place> nearbyPlaces = getNearbyPlaces(latitude, place, longitude, dist);
            List<Place> topRatedPlaces = getTopRatedPlaces(nearbyPlaces);
            List<Place> budgetFilteredPlaces = filterplacesByBudget(nearbyPlaces, budget);
            return topRatedPlaces;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<Place> getNearbyPlaces(String latitude, String place, String longitude, String radius)
            throws Exception {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + ","
                + longitude + "&radius=" + radius + "&keyword=" + place + "&key=" + context.getApiKey();
        return fetchPlaces(url);
    }

    private static List<Place> getTopRatedPlaces(List<Place> places) {
        List<Place> topRatedPlaces = new ArrayList<>();
        for (Place place : places) {
            if (place.getRating() >= 4) {
                topRatedPlaces.add(place);
            }
        }
        return topRatedPlaces;
    }

    private static List<Place> filterplacesByBudget(List<Place> places, String budget) {
        List<Place> filteredPlaces = new ArrayList<>();
        float Budget = Float.parseFloat(budget);
        for (Place place : places) {
            int price = place.getPriceLevel();
            if (price <= Budget) {
                filteredPlaces.add(place);
            }
        }
        return filteredPlaces;
    }

    private static List<Place> fetchPlaces(String url) throws Exception {
        List<Place> places = new ArrayList<>();
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            json.append(line);
        }
        conn.disconnect();
        JSONObject response = new JSONObject(json.toString());
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String placeId = result.getString("place_id");
            PlaceDetails placeDetails = PlacesApi.placeDetails(context, placeId).await();
            Place place = new Place(placeDetails.name, placeDetails.rating, placeDetails.formattedPhoneNumber,
                    placeDetails.vicinity);
            places.add(place);
        }
        return places;
    }
}

class Place {
    private String name;
    private float rating;
    private String phoneNumber;
    private String address;
    private int priceLevel;

    public Place(String name, float rating, String phoneNumber, String address, int priceLevel) {
        this.name = name;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.priceLevel = priceLevel;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    @Override
    public String toString() {
        return "Place: " + name + ", Rating: " + rating + ", Phone: " + phoneNumber + ", Address: " + address
                + ",Price: " + priceLevel;
    }
}