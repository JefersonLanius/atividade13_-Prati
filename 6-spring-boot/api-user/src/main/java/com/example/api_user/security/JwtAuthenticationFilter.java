package com.example.api_user.security;

import com.example.api_user.service.CustomUserDetailsService;

// Importa classes para manipulação de requisições e respostas HTTP em um ambiente de servlet.
import jakarta.servlet.FilterChain; // Representa a cadeia de filtros que a requisição precisa percorrer.
import jakarta.servlet.ServletException; // Exceção que pode ser lançada durante o processamento de requisições.
import jakarta.servlet.http.HttpServletRequest; // Representa a requisição HTTP.
import jakarta.servlet.http.HttpServletResponse; // Representa a resposta HTTP.

// Importa o pacote de configuração do Spring.
// A anotação @Configuration serve para registrar a classe no contexto do Spring.
import org.springframework.context.annotation.Configuration;

// Importa classes do Spring Security relacionadas à autenticação.
// UsernamePasswordAuthenticationToken é o token que o Spring Security usa para autenticar o usuário baseado em nome de usuário e senha.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// Importa o SecurityContextHolder, que armazena as informações de segurança (autenticação) durante a requisição atual.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa o UserDetails, que contém as informações sobre o usuário autenticado.
import org.springframework.security.core.userdetails.UserDetails;

// Importa uma classe que captura detalhes da autenticação da requisição (como o IP, navegador, etc.).
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// Importa a classe que permite a criação de filtros para serem aplicados em cada requisição.
import org.springframework.web.filter.OncePerRequestFilter; // Garante que o filtro será executado apenas uma vez por requisição.

// Importa a classe IOException, lançada caso ocorra um erro de I/O durante o processamento da requisição.
import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        //String username = jwtTokenProvider.extractUsername(jwt);

        String id = jwtTokenProvider.extractId(jwt);

        UserDetails userDetails = null;

        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userDetails = userDetailsService.loadUserById(id);
        }

        UsernamePasswordAuthenticationToken authenticationToken = null;
        if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}