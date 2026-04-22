package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CadastroController {

    private final UsuarioService usuarioService;

    public CadastroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/cadastro")
    public String abrirCadastro(Model model,
                                @RequestParam(value = "erro", required = false) String erro) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("erro", erro);
        return "cadastro";
    }

    @PostMapping("/cadastro/salvar")
    public String salvarCadastro(@ModelAttribute Usuario usuario) {
        try {
            if (!"ALUNO".equals(usuario.getTipoUsuario()) && !"PROFESSOR".equals(usuario.getTipoUsuario())) {
                throw new RuntimeException("No cadastro público só é permitido aluno ou professor");
            }

            usuarioService.salvar(usuario);
            return "redirect:/login?cadastro=true";
        } catch (RuntimeException e) {
            return "redirect:/cadastro?erro=" + e.getMessage().replace(" ", "%20");
        }
    }

@GetMapping("/acesso-negado")
public String acessoNegado(org.springframework.security.core.Authentication authentication, Model model) {
    String destino = "/login";

    if (authentication != null) {
        boolean funcionario = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FUNCIONARIO"));

        if (funcionario) {
            destino = "/admin";
        } else {
            destino = "/meus-emprestimos";
        }
    }

    model.addAttribute("destinoVoltar", destino);
    return "acesso-negado";
}
}