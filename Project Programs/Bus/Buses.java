import java.io.*;
import java.net.*;
import javax.servlet.*;

class BusesServlet extends HttpServlet {
    private static final String API_KEY = "";

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String current_location = req.getParameter("Current Location");
        String final_location = req.getParameter("Final Location");
        String API_URL = "";
        String apiKeyParameter = "?key=" + API_KEY;
        String originParameter = "?origin=" + current_location;
        String destinationParameter = "?destination=" + final_location;
        String modeParamter = "?mode=transit";
        String finalUrl = API_URL + apiKeyParameter + originParameter + destinationParameter + modeParamter;
        URL url = new URL(finalUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder json_response = new StringBuilder();
        String text;
        while ((text = reader.readLine()) != null) {
            json_response.append(text);
        }
        reader.close();
    }

}
