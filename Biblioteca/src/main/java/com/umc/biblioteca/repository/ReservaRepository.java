package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.Reserva;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservaRepository extends MongoRepository<Reserva, String> {
    List<Reserva> findByUsuarioId(String usuarioId);
    List<Reserva> findByLivroIdAndStatusOrderByDataSolicitacaoAsc(String livroId, String status);
    Optional<Reserva> findByLivroIdAndUsuarioIdAndStatus(String livroId, String usuarioId, String status);
    List<Reserva> findByUsuarioIdAndStatusIn(String usuarioId, List<String> status);
}