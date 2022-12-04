package ru.job4j.cars.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Фильтр проверки прав доступа пользователя к контенту сайта
 */
@Component
public class AuthFilter implements Filter {

    /**
     * Сет из окончаний строк URI, при котором пользователю, не прошедшему
     * аутентификацию на сайте, доступно содержимое запрошенного контента
     */
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

    /**
     * Regular expression, применённое к URI, позволяющее пользователю,
     * не прошедшему аутентификацию на сайте, просматривать контент в виде
     * фотографий автомобилей
     */
    private static final Pattern PATTERN = Pattern.compile("/postPhoto/.+");

    /**
     * Проверка соответствия URI доступности содержимого контента сайта для
     * пользователя, не прошедшего аутентификацию
     * @param uri URI
     * @return результат проверки
     */
    private boolean isEnds(String uri) {
        return URI_SUFFIXES.stream().anyMatch(uri::endsWith)
                || PATTERN.matcher(uri).matches();
    }

    /**
     * Реализация метода фильтрации
     * @param request объект запроса в виде HttpServletRequest
     * @param response объект ответа в виде HttpServletResponse
     * @param chain объект FilterChain
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (isEnds(uri)) {
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
