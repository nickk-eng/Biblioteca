package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.Emprestimo;
import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.service.EmprestimoService;
import com.umc.biblioteca.service.LivroService;
import com.umc.biblioteca.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    public EmprestimoController(EmprestimoService emprestimoService,
                                LivroService livroService,
                                UsuarioService usuarioService) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/emprestimos")
    public String listarEmprestimos(Model model,
                                    @RequestParam(value = "erro", required = false) String erro,
                                    @RequestParam(value = "sucesso", required = false) String sucesso) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("erro", erro);
        model.addAttribute("sucesso", sucesso);
        return "emprestimos";
    }

    @GetMapping("/meus-emprestimos")
    public String meusEmprestimos(Model model, Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorLogin(authentication.getName());
        model.addAttribute("emprestimos", emprestimoService.listarPorUsuario(usuario.getId()));
        model.addAttribute("usuarioLogado", usuario);
        return "meus-emprestimos";
    }

    @PostMapping("/emprestimos/salvar")
    public String salvarEmprestimo(@RequestParam String livroId,
                                   @RequestParam String usuarioId,
                                   @RequestParam int diasParaDevolucao) {
        try {
            emprestimoService.realizarEmprestimo(livroId, usuarioId, diasParaDevolucao);
            return "redirect:/emprestimos?sucesso=Empréstimo realizado com sucesso";
        } catch (RuntimeException e) {
            return "redirect:/emprestimos?erro=" + e.getMessage().replace(" ", "%20");
        }
    }

    @GetMapping("/emprestimos/devolver/{id}")
    public String devolverLivro(@PathVariable String id) {
        try {
            emprestimoService.devolverLivro(id);
            return "redirect:/emprestimos?sucesso=Livro devolvido com sucesso";
        } catch (RuntimeException e) {
            return "redirect:/emprestimos?erro=" + e.getMessage().replace(" ", "%20");
        }
    }
}