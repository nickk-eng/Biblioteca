package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.Livro;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LivroRepository extends MongoRepository<Livro, String> {
}