package com.example.Servlets;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



@WebServlet(urlPatterns = "/time")
public class TimeServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
        String timezone = req.getParameter("timezone");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zzz");
        ZonedDateTime zonedDateTime;
        try (PrintWriter out = resp.getWriter()){
            if (timezone == null || timezone.isEmpty()) {
                zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
            } else {
                zonedDateTime = ZonedDateTime.now(ZoneId.of(timezone));
            }
            out.println("Current time: " + zonedDateTime.format(formatter));
        } catch (IOException e){
            System.out.println("Unknown error");
        }
    }
}
