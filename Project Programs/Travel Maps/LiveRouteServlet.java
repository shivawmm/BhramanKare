import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class LiveRouteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        if (start == null || start.isEmpty() || end == null || end.isEmpty()) {
            response.getStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Please providew both Starting And Fianl Destination of your journey");
            return;
        }
        GeoApiContext context = new GeoApiContext().setAPiKey("API KEy");
        DirectionsResult directionResults;
        try {
            directionResults = DirectionsApi.getDirections(context, start, end).mode("driving").await();
        } catch (ApiException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("Failed to fetch Directions on your route" + e.getMessage());
            out.close();
            return;
        }
        DirectionsRoute route = directionsResult.routes[0];
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Live Maps</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Route from " + start + " to " + end + "</h1>");
        out.println("<ul>");
        for (DirectionsStep step : route.legs[0].steps) {
            out.println("<li>" + step.instructions + "</li>");
        }
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }

}
