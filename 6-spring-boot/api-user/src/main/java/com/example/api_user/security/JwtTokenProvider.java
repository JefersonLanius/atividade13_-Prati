package com.example.api_user.security;

import com.example.api_user.model.User;
import io.jsonwebtoken.Claims; // Representa as "claims" (declarações) de um token JWT. Isso inclui informações como o emissor, o assunto, a data de expiração, etc.
import io.jsonwebtoken.Jwts; // Classe usada para criar, analisar e validar tokens JWT.
import io.jsonwebtoken.SignatureAlgorithm; // Enum que define o algoritmo de assinatura usado para criptografar o token JWT (neste caso, HS256).

// Importa classes do Spring para gerenciar propriedades e autenticação.
import org.springframework.beans.factory.annotation.Value; // Permite injetar valores de propriedades do arquivo application.properties diretamente em variáveis de classe.
import org.springframework.security.core.userdetails.UserDetails; // Interface que contém informações sobre o usuário, como nome de usuário e permissões.

// Marca a classe como um bean Spring que será gerenciado pelo contêiner de IoC (Inversão de Controle).
import org.springframework.stereotype.Component;

// Importa classes Java para manipulação de datas, mapas e funções.
import java.util.Date; // Classe usada para representar uma data no Java.
import java.util.HashMap; // Implementação da interface Map, usada para armazenar pares de chave/valor.
import java.util.Map; // Interface que define um mapa (associação de chave-valor) no Java.
import java.util.function.Function; // Interface funcional que aplica uma transformação a um dado e retorna um resultado.

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, user.getUsername(), String.valueOf(user.getId()));
    }

    private String createToken(Map<String, Object> claims, String subject, String id) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiração: 10 horas
                .signWith(SignatureAlgorithm.HS256, secretKey) // Assinatura com algoritmo HS256 e chave secreta
                .compact(); // Compacta e retorna o token JWT
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica se o token não expirou e se o usuário é o correto
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Verifica se a data de expiração é anterior à data atual
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extrai a claim de expiração (expiration)
    }
}