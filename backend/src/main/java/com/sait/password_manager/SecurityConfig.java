package com.sait.password_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // Wir injizieren unseren Filter

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .cors(withDefaults())

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // NEUES BEAN: CORS-Konfiguration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Wir erlauben unsere Frontend-Adresse
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // Erlaubte HTTP-Methoden
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        // Erlaubte Header
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Diese Einstellungen für alle Pfade anwenden
        return source;
    }


}
/*Kısa Özet
Bean: Spring konteynerinin yaşam döngüsünü yönettiği bir Java nesnesidir.
@Bean: Bir metodu, Spring için bir Bean üreten fabrikaya dönüştürür. Nesneyi Spring'e verir.
@Autowired: Spring konteynerinden yönetilen bir Bean'i talep eder ve bir alana/constructora enjekte eder. Nesneyi Spring'den alır.
Bu ikili, Spring'in temelini oluşturan Dependency Injection (DI) mekanizmasını hayata geçirir ve
bileşenlerin birbirine olan bağımlılığını azaltarak (gevşek bağlılık - loose coupling) esnek bir mimari sunar.

Kurze Zusammenfassung
Bean: Ein Java-Objekt, dessen Lebenszyklus vom Spring-Container verwaltet wird.

@Bean: Verwandelt eine Methode in eine Fabrik, die eine Bean für Spring produziert. Sie gibt ein Objekt an Spring.

@Autowired: Fordert eine verwaltete Bean vom Spring-Container an und injiziert sie in ein Feld/einen Konstruktor. Sie nimmt ein Objekt von Spring.

Dieses Duo bildet die Grundlage des Dependency Injection (DI)-Mechanismus von Spring und ermöglicht eine flexible Architektur,
indem es die Komponenten lose koppelt (loose coupling).*/