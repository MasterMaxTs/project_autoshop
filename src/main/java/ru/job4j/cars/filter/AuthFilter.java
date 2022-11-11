package ru.job4j.cars.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class AuthFilter implements Filter {

    private static final Set<String> URI_SUFFIXES = Set.of("index",
                                                           "posts",
                                                           "formAddUser",
                                                           "addUser",
                                                           "loginPage",
                                                           "login",
                                                           "edit",
                                                           "last_day",
                                                           "by_parameters",
                                                           "by_brand_price",
                                                           "archive");
    private static final Pattern PATTERN = Pattern.compile("/postPhoto/.+");

    private boolean isEnds(String uri) {
        return URI_SUFFIXES.stream().anyMatch(uri::endsWith);
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (isEnds(uri) || PATTERN.matcher(uri).matches()) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);

    }
}
