package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.Usuario;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByLogin(String login);
}