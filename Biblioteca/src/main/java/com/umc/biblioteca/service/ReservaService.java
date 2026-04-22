package com.umc.biblioteca.service;

import com.umc.biblioteca.model.Emprestimo;
import com.umc.biblioteca.model.Livro;
import com.umc.biblioteca.model.Reserva;
import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.repository.EmprestimoRepository;
import com.umc.biblioteca.repository.ReservaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    public ReservaService(ReservaRepository reservaRepository,
                          EmprestimoRepository emprestimoRepository,
                          LivroService livroService,
                          UsuarioService usuarioService) {
        this.reservaRepository = reservaRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> listarPorUsuario(String usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public List<Reserva> listarHistoricoPorUsuario(String usuarioId) {
        return reservaRepository.findByUsuarioIdAndStatusIn(
                usuarioId,
                List.of("ATENDIDA", "CANCELADA", "EXPIRADA")
        );
    }

    public Reserva buscarPorId(String id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }

    public Reserva reservarLivro(String livroId, String usuarioId, int diasExpiracao) {
        Livro livro = livroService.buscarPorId(livroId);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        if (livro.getEstoque() != null && livro.getEstoque() > 0) {
            throw new RuntimeException("Este livro está disponível em estoque e não precisa de reserva");
        }

        if (diasExpiracao <= 0) {
            throw new RuntimeException("Informe uma quantidade de dias válida");
        }

        if (reservaRepository.findByLivroIdAndUsuarioIdAndStatus(livroId, usuarioId, "ATIVA").isPresent()) {
            throw new RuntimeException("Este usuário já possui uma reserva ativa para este livro");
        }

        Reserva reserva = new Reserva();
        reserva.setLivroId(livro.getId());
        reserva.setUsuarioId(usuario.getId());
        reserva.setTituloLivro(livro.getTitulo());
        reserva.setNomeUsuario(usuario.getNome());
        reserva.setDataSolicitacao(LocalDate.now());
        reserva.setDataExpiracao(LocalDate.now().plusDays(diasExpiracao));
        reserva.setStatus("ATIVA");

        return reservaRepository.save(reserva);
    }

    public Reserva cancelarReserva(String id) {
        Reserva reserva = buscarPorId(id);
        reserva.setStatus("CANCELADA");
        return reservaRepository.save(reserva);
    }

    public Reserva expirarReserva(String id) {
        Reserva reserva = buscarPorId(id);
        reserva.setStatus("EXPIRADA");
        return reservaRepository.save(reserva);
    }

    public void excluir(String id) {
        reservaRepository.deleteById(id);
    }

    public void processarReservaAutomatica(String livroId) {
        Livro livro = livroService.buscarPorId(livroId);

        if (livro.getEstoque() == null || livro.getEstoque() <= 0) {
            return;
        }

        List<Reserva> reservasAtivas = reservaRepository.findByLivroIdAndStatusOrderByDataSolicitacaoAsc(livroId, "ATIVA");

        for (Reserva reserva : reservasAtivas) {
            livro = livroService.buscarPorId(livroId);

            if (livro.getEstoque() == null || livro.getEstoque() <= 0) {
                return;
            }

            if (reserva.getDataExpiracao() != null && reserva.getDataExpiracao().isBefore(LocalDate.now())) {
                reserva.setStatus("EXPIRADA");
                reservaRepository.save(reserva);
                continue;
            }

            Usuario usuario = usuarioService.buscarPorId(reserva.getUsuarioId());

            livroService.reduzirEstoque(livroId);

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setLivroId(livro.getId());
            emprestimo.setUsuarioId(usuario.getId());
            emprestimo.setTituloLivro(livro.getTitulo());
            emprestimo.setNomeUsuario(usuario.getNome());
            emprestimo.setDataEmprestimo(LocalDate.now());
            emprestimo.setDataPrevistaDevolucao(LocalDate.now().plusDays(7));
            emprestimo.setSituacao("ATIVO");
            emprestimo.setMulta(0.0);

            emprestimoRepository.save(emprestimo);

            reserva.setStatus("ATENDIDA");
            reserva.setDataAtendimento(LocalDate.now());
            reservaRepository.save(reserva);
        }
    }
}