package com.example.api_user.dto;


import lombok.AllArgsConstructor; // Gera automaticamente um construtor que aceita argumentos para todos os campos da classe.
import lombok.Data; // Gera automaticamente getters, setters, equals, hashCode, toString e outros métodos úteis.
import lombok.NoArgsConstructor; // Gera automaticamente um construtor vazio (sem parâmetros).

@AllArgsConstructor

@NoArgsConstructor

@Data
public class LoginDTO {

    private String username; // Nome de usuário (identificador usado no processo de login)
    private String password; // Senha do usuário (usada para autenticação)
}