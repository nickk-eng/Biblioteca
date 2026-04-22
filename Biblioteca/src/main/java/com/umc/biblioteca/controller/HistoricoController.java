package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.service.EmprestimoService;
import com.umc.biblioteca.service.ReservaService;
import com.umc.biblioteca.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoricoController {

    private final EmprestimoService emprestimoService;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    public HistoricoController(EmprestimoService emprestimoService,
                               ReservaService reservaService,
                               UsuarioService usuarioService) {
        this.emprestimoService = emprestimoService;
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/historico")
    public String historico(Model model, Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorLogin(authentication.getName());
        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("historicoEmprestimos", emprestimoService.listarHistoricoPorUsuario(usuario.getId()));
        model.addAttribute("historicoReservas", reservaService.listarHistoricoPorUsuario(usuario.getId()));
        return "historico";
    }
}