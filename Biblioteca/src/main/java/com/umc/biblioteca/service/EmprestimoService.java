package com.umc.biblioteca.service;

import com.umc.biblioteca.model.Emprestimo;
import com.umc.biblioteca.model.Livro;
import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.repository.EmprestimoRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final ReservaService reservaService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             LivroService livroService,
                             UsuarioService usuarioService,
                             ReservaService reservaService) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
        this.reservaService = reservaService;
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    public List<Emprestimo> listarPorUsuario(String usuarioId) {
        return emprestimoRepository.findByUsuarioId(usuarioId);
    }

    public List<Emprestimo> listarHistoricoPorUsuario(String usuarioId) {
        return emprestimoRepository.findByUsuarioIdAndSituacao(usuarioId, "DEVOLVIDO");
    }

    public Emprestimo buscarPorId(String id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
    }

    public Emprestimo realizarEmprestimo(String livroId, String usuarioId, int diasParaDevolucao) {
        Livro livro = livroService.buscarPorId(livroId);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        if (livro.getEstoque() == null || livro.getEstoque() <= 0) {
            throw new RuntimeException("Livro sem estoque disponível");
        }

        if (diasParaDevolucao <= 0) {
            throw new RuntimeException("Informe uma quantidade de dias válida");
        }

        livroService.reduzirEstoque(livroId);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivroId(livro.getId());
        emprestimo.setUsuarioId(usuario.getId());
        emprestimo.setTituloLivro(livro.getTitulo());
        emprestimo.setNomeUsuario(usuario.getNome());
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().plusDays(diasParaDevolucao));
        emprestimo.setSituacao("ATIVO");
        emprestimo.setMulta(0.0);

        return emprestimoRepository.save(emprestimo);
    }

    public Emprestimo devolverLivro(String id) {
        Emprestimo emprestimo = buscarPorId(id);

        if ("DEVOLVIDO".equals(emprestimo.getSituacao())) {
            throw new RuntimeException("Este livro já foi devolvido");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setSituacao("DEVOLVIDO");
        emprestimo.setMulta(calcularMulta(emprestimo.getDataPrevistaDevolucao(), emprestimo.getDataDevolucao()));

        livroService.aumentarEstoque(emprestimo.getLivroId());
        emprestimoRepository.save(emprestimo);

        reservaService.processarReservaAutomatica(emprestimo.getLivroId());

        return emprestimo;
    }

    public double calcularMulta(LocalDate dataPrevista, LocalDate dataDevolucao) {
        if (dataDevolucao == null || !dataDevolucao.isAfter(dataPrevista)) {
            return 0.0;
        }

        long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataDevolucao);
        return diasAtraso * 2.0;
    }
}