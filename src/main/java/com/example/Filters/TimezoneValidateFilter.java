package com.example.Filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@WebFilter(value = "/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String timezone = servletRequest.getParameter("timezone");
        if (timezone == null || timezone.isEmpty()) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            try {
                ZoneId zone = ZoneId.of(timezone);
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (DateTimeException e) {
                if (servletResponse instanceof HttpServletResponse) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                    httpServletResponse.setStatus(400);
                    PrintWriter printWriter = servletResponse.getWriter();
                    printWriter.println("Invalid timezone");
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            }
        }
    }

    @Override
    public void destroy() {

    }
}
