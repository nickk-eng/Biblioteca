package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.Emprestimo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmprestimoRepository extends MongoRepository<Emprestimo, String> {
    List<Emprestimo> findByUsuarioId(String usuarioId);
    List<Emprestimo> findByUsuarioIdAndSituacao(String usuarioId, String situacao);
}