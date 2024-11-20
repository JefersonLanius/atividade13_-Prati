package com.example.api_user.security;

import com.example.api_user.service.CustomUserDetailsService; // Serviço personalizado para carregar os detalhes do usuário.
import org.springframework.context.annotation.Bean; // Define que um método cria um bean gerenciado pelo Spring.
import org.springframework.context.annotation.Configuration; // Marca a classe como uma classe de configuração do Spring.
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Gerencia o processo de autenticação no Spring Security.
import org.springframework.security.authentication.AuthenticationProvider; // Interface que autentica um objeto com base em uma estratégia de autenticação.
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Implementa um AuthenticationProvider usando o padrão DAO (Data Access Object).
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Permite configurar a autenticação.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Permite configurar a segurança da aplicação, como endpoints protegidos.
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Habilita a segurança baseada em web no Spring Security.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Classe para criptografar senhas usando o algoritmo BCrypt.
import org.springframework.security.crypto.password.PasswordEncoder; // Interface usada para criptografar e verificar senhas.
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter; // Converte tokens JWT em autenticações Spring Security.
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter; // Converte claims JWT em autoridades do Spring Security.
import org.springframework.security.web.SecurityFilterChain; // Define a cadeia de filtros de segurança no Spring Security.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filtro que processa a autenticação baseada em nome de usuário e senha.

@Configuration

@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // Filtro de autenticação JWT para verificar tokens em cada requisição.
    private final CustomUserDetailsService customUserDetailsService; // Serviço personalizado que carrega os detalhes do usuário a partir do banco de dados.

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();


        authProvider.setUserDetailsService(customUserDetailsService);


        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())


                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/users").permitAll()

                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}