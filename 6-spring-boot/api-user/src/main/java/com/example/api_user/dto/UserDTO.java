package com.example.api_user.dto;


import lombok.Data;

@Data
public class UserDTO {
    // Atributos da classe que representam os dados do usuário transferidos pela API (DTO - Data Transfer Object)
    private int id; // O ID único do usuário, geralmente gerado pelo banco de dados.
    private String username; // Nome de usuário para identificação e login.
    private String email; // Endereço de e-mail do usuário.
    private String role; // O papel (role) do usuário, como "ROLE_USER" ou "ROLE_ADMIN".
    private String password; // Senha do usuário, que será criptografada quando manipulada no sistema.
}