package org.mykhailo.todo_backend_mysql.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // JWT фильтр для обработки токенов в запросах
    @Autowired
    private JwtFilter jwtFilter;

    // Сервис для загрузки информации о пользователях
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Конфигурация цепочки фильтров безопасности.
     *
     * @param http HttpSecurity объект для настройки безопасности
     * @return Конфигурированная цепочка фильтров
     * @throws Exception Возможные исключения при конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable()) // Отключение защиты от CSRF
                .authorizeHttpRequests(request -> request
                        // Разрешить доступ к эндпоинтам для входа и регистрации без аутентификации
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // Для всех остальных запросов требуется аутентификация
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()) // Использование базовой HTTP-аутентификации
                // Установка политики управления сессиями как "без состояния" (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Добавление JWT фильтра перед UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Конфигурация AuthenticationProvider для обработки аутентификации пользователей.
     * Использует BCryptPasswordEncoder для хэширования паролей и UserDetailsService
     * для загрузки информации о пользователях.
     *
     * @return Настроенный AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Установка кодировщика паролей
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        // Установка сервиса для загрузки пользователей
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /**
     * Конфигурация AuthenticationManager для управления процессом аутентификации.
     *
     * @param config AuthenticationConfiguration объект конфигурации
     * @return AuthenticationManager для обработки аутентификации
     * @throws Exception Возможные исключения при создании менеджера
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Оставлен для примера: демонстрация создания пользователей в памяти (InMemoryUserDetailsManager)
    // Используется для тестирования или простых приложений
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails user1 = User
    //             .withDefaultPasswordEncoder()
    //             .username("kiran")
    //             .password("k@123")
    //             .roles("USER")
    //             .build();
    //
    //     UserDetails user2 = User
    //             .withDefaultPasswordEncoder()
    //             .username("harsh")
    //             .password("h@123")
    //             .roles("ADMIN")
    //             .build();
    //
    //     return new InMemoryUserDetailsManager(user1, user2);
    // }
}
