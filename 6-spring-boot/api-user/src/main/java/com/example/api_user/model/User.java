package com.example.api_user.model;
import jakarta.persistence.*; // Importa todas as anotações necessárias para JPA (Java Persistence API), como @Entity, @Id, @GeneratedValue, etc.
import lombok.Data; // Importa a anotação @Data do Lombok, que gera automaticamente getters, setters, equals, hashCode, toString e outros métodos úteis.

@Entity

@Data

@Table(name = "users")
public class User {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Campo que armazena o ID único de cada usuário.

    @Column(nullable = false)
    private String username; // Nome de usuário, que é obrigatório.

    @Column(nullable = false)
    private String password; // Senha do usuário, que é obrigatória.

    @Column(nullable = false)
    private String email; // Endereço de e-mail do usuário, que é obrigatório.

    @Column(nullable = false)
    private String role; // Papel ou função do usuário na aplicação, como "ROLE_USER" ou "ROLE_ADMIN".
}