package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.Livro;
import com.umc.biblioteca.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public String listarLivros(Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("livro", new Livro());
        return "livros";
    }

    @PostMapping("/salvar")
    public String salvarLivro(@ModelAttribute Livro livro) {
        livroService.salvar(livro);
        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    public String editarLivro(@PathVariable String id, Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("livro", livroService.buscarPorId(id));
        return "livros";
    }

    @GetMapping("/excluir/{id}")
    public String excluirLivro(@PathVariable String id) {
        livroService.excluir(id);
        return "redirect:/livros";
    }
}