import javax.management.Descriptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Weather extends HttpServlet {
    private static final Properties prop = new Properties();
    private static final String apikey;
    static {
        try (InputStream input = Weather.class.getResourceAsStream("configure.properties")) {
            prop.load(input);
            apikey = prop.getProperty("api_key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String location = req.getParameter("location");
        if (location == null || location.isEmpty()) {
            resp.getStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Please give the location, it seems you have not given the location");
            return;
        }
        try {
            String apiUrl = "https://community-open-weather-map.p.rapidapi.com/data/2.5/weather?q=" + location;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com");
            connection.setRequestProperty("x-rapidapi-host", apikey);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ObjectMapper obj = new ObjectMapper();
                Map<String, Object> weather_data = obj.readvalue(connection.getInputStream(), Map.class);
                Map<String, Object> main = (Map<String, Object>) weather_data.get("main");
                String temperature = main.get("temp").toString();
                List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weather_data.get("weather");
                String description = weatherList.get(0).get("description").toString();
                resp.setContent("application/json");
                PrintWriter out = resp.getWriter();
                Map<String, Object> responseMap = new HashMap<>();
                Map<String, Object> currentweather = new HashMap<>();
                currentweather.put("temperature", temperature);
                currentweather.put("description", description);
                responseMap.put("current-weather", currentweather);
                ObjectMapper.writeValue(out, responseMap);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("No such location found try again");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An Error has been  encountered");
        }
    }

}
