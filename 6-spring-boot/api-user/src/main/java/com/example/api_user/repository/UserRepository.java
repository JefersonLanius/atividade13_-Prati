package com.example.api_user.repository;

import org.springframework.data.jpa.repository.JpaRepository; // Interface que fornece métodos CRUD e mais para trabalhar com persistência JPA.
import com.example.api_user.model.User; // A classe modelo que representa a entidade User no banco de dados.
import org.springframework.stereotype.Repository; // Anotação que marca esta interface como um repositório, uma especialização de um componente Spring.

import java.util.Optional; // Classe que pode ou não conter um valor, usada para evitar null pointers.

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
