package com.example.Servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



@WebServlet(urlPatterns = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;
    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix(getServletContext().getRealPath("/WEB-INF/templates/"));
        resolver.setSuffix(".html");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String timezone = req.getParameter("timezone");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zzz");
        ZonedDateTime zonedDateTime = null;
        try (PrintWriter out = resp.getWriter()){
            if (timezone == null || timezone.isEmpty()) {
                Cookie[] cookies = req.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("lastTimezone")) {
                        timezone = cookie.getValue();
                        zonedDateTime = ZonedDateTime.now(ZoneId.of(timezone));
                    } else {
                        zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
                    }
                }
            } else {
                resp.addCookie(new Cookie("lastTimezone", timezone));
                zonedDateTime = ZonedDateTime.now(ZoneId.of(timezone));
            }
            Context context = new Context();

            assert zonedDateTime != null;
            String time = zonedDateTime.format(formatter);
            context.setVariable("currentTime", time);
            engine.process("time", context, out);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
