package org.intech.reservation.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.intech.reservation.utils.Utils.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final JWTAuthConverter jwtAuthConverter;
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v2/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String adminRole = "flight_admin";
        String customerRole = "flight_customer";
        String employeeRole = "flight_employee";


        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                WHITE_LIST_URL
                        )

                .permitAll()
                .requestMatchers(rootEndpoint + "/**").hasAnyRole(adminRole, employeeRole, customerRole)

                .requestMatchers(HttpMethod.GET, rootEndpoint + customerEndpoint)
                .hasAnyAuthority(adminRole, employeeRole)
                .requestMatchers(HttpMethod.POST, rootEndpoint + customerEndpoint )
                .hasAnyAuthority(adminRole, employeeRole)
                .requestMatchers(HttpMethod.PUT, rootEndpoint + customerEndpoint )
                .hasAnyAuthority(adminRole, employeeRole)
                .requestMatchers(HttpMethod.DELETE, rootEndpoint + customerEndpoint )
                .hasAnyAuthority(adminRole, employeeRole)

                .requestMatchers(HttpMethod.GET, rootEndpoint + flightEndpoint)
                .hasAnyAuthority(adminRole, employeeRole)
                .requestMatchers(HttpMethod.POST, rootEndpoint + flightEndpoint )
                .hasAnyAuthority(adminRole, employeeRole)
                .requestMatchers(HttpMethod.PUT, rootEndpoint + flightEndpoint )
                .hasAnyAuthority(adminRole, employeeRole)
                .requestMatchers(HttpMethod.DELETE, rootEndpoint + flightEndpoint )
                .hasAnyAuthority(adminRole, employeeRole)

                .requestMatchers(HttpMethod.POST, rootEndpoint + reservationEndpoint)
                .hasAnyAuthority(adminRole, employeeRole, customerRole)

                .anyRequest()
                .authenticated()
                );

        httpSecurity
                .oauth2ResourceServer(oauth2 ->
                        oauth2
                                .jwt(jwtConfigurer ->
                                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter)
                                ));

        httpSecurity
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return httpSecurity.build();
    }
}
