
package com.example.api_user.service;

import com.example.api_user.model.User; // Classe modelo que representa um usuário no banco de dados.
import com.example.api_user.repository.UserRepository; // Interface para realizar operações de banco de dados com a entidade User.
import org.springframework.beans.factory.annotation.Autowired; // Injeta automaticamente dependências (beans) do Spring.
import org.springframework.security.core.userdetails.UserDetails; // Interface que representa os detalhes de um usuário para autenticação.
import org.springframework.security.core.userdetails.UserDetailsService; // Interface que define um serviço que carrega dados do usuário para autenticação.
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exceção lançada quando o nome de usuário não é encontrado.
import org.springframework.stereotype.Service; // Anotação que marca a classe como um serviço gerenciado pelo Spring.

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername()) // Define o nome de usuário
                .password(user.getPassword()) // Define a senha (criptografada) do usuário
                .build(); // Constrói o objeto UserDetails
    }

    public  UserDetails loadUserById(String id) throws UsernameNotFoundException{
        User user = userRepository.findById(Integer.valueOf(id)).get();

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + id);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .build();

    }
}