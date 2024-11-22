package org.mykhailo.todo_backend_mysql.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mykhailo.todo_backend_mysql.service.JwtService;
import org.mykhailo.todo_backend_mysql.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Сервис для работы с JWT токенами
    @Autowired
    private JwtService jwtService;

    // Контекст приложения для получения бинов
    @Autowired
    ApplicationContext context;

    /**
     * Основной метод фильтрации запросов. Выполняет проверку JWT токена
     *
     * @param request Запрос HTTP
     * @param response Ответ HTTP
     * @param filterChain Цепочка фильтров
     * @throws ServletException Исключение сервлета
     * @throws IOException Исключение ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Извлечение заголовка авторизации
        String authHeader = request.getHeader("Authorization");
        String token = null; // Переменная для хранения JWT токена
        String username = null; // Переменная для хранения имени пользователя

        // Проверка наличия токена и его формата
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Извлечение токена без префикса "Bearer "
            username = jwtService.extractUserName(token); // Извлечение имени пользователя из токена
        }

        // Проверка, что пользователь еще не аутентифицирован в контексте безопасности
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Загрузка данных пользователя через UserService
            UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(username);

            // Проверка валидности токена
            if (jwtService.validateToken(token, userDetails)) {
                // Создание объекта аутентификации
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Добавление информации о запросе
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Установка объекта аутентификации в контекст безопасности
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Продолжение выполнения цепочки фильтров
        filterChain.doFilter(request, response);
    }
}