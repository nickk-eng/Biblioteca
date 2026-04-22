package com.umc.biblioteca.service;

import com.umc.biblioteca.model.Livro;
import com.umc.biblioteca.repository.LivroRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(String id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }

    public Livro salvar(Livro livro) {
        if (livro.getId() != null && livro.getId().isBlank()) {
            livro.setId(null);
        }

        if (livro.getEstoque() == null || livro.getEstoque() < 0) {
            livro.setEstoque(0);
        }

        return livroRepository.save(livro);
    }

    public void excluir(String id) {
        livroRepository.deleteById(id);
    }

    public void reduzirEstoque(String id) {
        Livro livro = buscarPorId(id);

        if (livro.getEstoque() == null || livro.getEstoque() <= 0) {
            throw new RuntimeException("Livro sem estoque disponível");
        }

        livro.setEstoque(livro.getEstoque() - 1);
        livroRepository.save(livro);
    }

    public void aumentarEstoque(String id) {
        Livro livro = buscarPorId(id);

        if (livro.getEstoque() == null) {
            livro.setEstoque(0);
        }

        livro.setEstoque(livro.getEstoque() + 1);
        livroRepository.save(livro);
    }
}