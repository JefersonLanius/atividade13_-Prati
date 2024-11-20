
package com.example.api_user.service;


import com.example.api_user.repository.UserRepository; // Repositório que permite a comunicação com o banco de dados para a entidade User.
import org.apache.catalina.authenticator.BasicAuthenticator; // Importação desnecessária para este contexto (não está sendo utilizada).
import org.springframework.beans.factory.annotation.Autowired; // Injeta automaticamente dependências (beans) gerenciadas pelo Spring.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Classe usada para criptografar senhas com o algoritmo BCrypt.
import org.springframework.stereotype.Service; // Marca a classe como um serviço Spring.

import com.example.api_user.dto.UserDTO; // Data Transfer Object (DTO) que encapsula os dados do usuário.
import com.example.api_user.model.User; // Classe modelo que representa a entidade User no banco de dados.

import java.util.List; // Interface da coleção List usada para armazenar múltiplos objetos.
import java.util.Optional; // Classe que pode ou não conter um valor, usada para evitar null pointers.
import java.util.stream.Collectors; // Coletor do pacote Stream usado para transformar fluxos de dados em coleções.


@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;


    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll() // Recupera todos os usuários do banco de dados como uma lista de objetos `User`.
                .stream() // Converte a lista em um fluxo (stream), que permite aplicar transformações nos dados.
                .map(this::convertToDTO) // Converte cada objeto `User` para um `UserDTO` chamando o método `convertToDTO`.
                .collect(Collectors.toList()); // Coleta os resultados e os transforma de volta em uma lista de objetos `UserDTO`.
    }


    public UserDTO getUserById(int id) {
        Optional<User> user = userRepository.findById(id); // Usa `Optional` para evitar null pointers.
        return user.map(this::convertToDTO).orElse(null); // Se o usuário estiver presente, converte para `UserDTO`, caso contrário, retorna null.
    }


    public UserDTO createUser(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());


        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

        userRepository.save(user);

        return convertToDTO(user);
    }


    public UserDTO updateUser(int id, UserDTO userDTO) {

        Optional<User> userOptional = userRepository.findById(id);


        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());


            user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            userRepository.save(user);
            return convertToDTO(user);
        }

        return null;
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id); // Remove o usuário do banco de dados usando seu ID.
    }


    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO(); // Cria uma nova instância de `UserDTO`.
        userDTO.setId(user.getId()); // Define o ID do usuário.
        userDTO.setUsername(user.getUsername()); // Define o nome de usuário.
        userDTO.setEmail(user.getEmail()); // Define o email.
        userDTO.setRole(user.getRole()); // Define o papel (role) do usuário.

        return userDTO;
    }
}