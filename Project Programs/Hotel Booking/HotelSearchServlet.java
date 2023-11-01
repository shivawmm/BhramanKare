package com.yourcompany.servlets;

import java.io.*;
import java.net.*;
import java.util.List;
import com.yourcompany.model.Hotel;
import javax.servlet.*;

public class HotelSearchservlet {
    protected void doPost(HttpServeltResponse resp, HttpServletRequest req) throws SerrvletException, IOException {
        String location = req.getParameter("location");

        List<Hotel> hotels = HotelAPIClient.searchHotels(location);
        req.setAttribut("hotels", hotels);
        req.getRequestDispatcher("/search-results.jsp").forward(req, resp);

    }
}
