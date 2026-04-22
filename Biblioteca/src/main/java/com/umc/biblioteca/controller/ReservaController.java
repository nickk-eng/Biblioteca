package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.service.LivroService;
import com.umc.biblioteca.service.ReservaService;
import com.umc.biblioteca.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservaController {

    private final ReservaService reservaService;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    public ReservaController(ReservaService reservaService,
                             LivroService livroService,
                             UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/reservas")
    public String listarReservas(Model model,
                                 @RequestParam(value = "erro", required = false) String erro,
                                 @RequestParam(value = "sucesso", required = false) String sucesso) {
        model.addAttribute("reservas", reservaService.listarTodas());
        model.addAttribute("livros", livroService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("erro", erro);
        model.addAttribute("sucesso", sucesso);
        return "reservas";
    }

    @GetMapping("/minhas-reservas")
    public String minhasReservas(Model model, Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorLogin(authentication.getName());
        model.addAttribute("reservas", reservaService.listarPorUsuario(usuario.getId()));
        model.addAttribute("usuarioLogado", usuario);
        return "minhas-reservas";
    }

    @PostMapping("/reservas/salvar")
    public String salvarReserva(@RequestParam String livroId,
                                @RequestParam String usuarioId,
                                @RequestParam int diasExpiracao) {
        try {
            reservaService.reservarLivro(livroId, usuarioId, diasExpiracao);
            return "redirect:/reservas?sucesso=Reserva realizada com sucesso";
        } catch (RuntimeException e) {
            return "redirect:/reservas?erro=" + e.getMessage().replace(" ", "%20");
        }
    }

    @GetMapping("/reservas/cancelar/{id}")
    public String cancelarReserva(@PathVariable String id) {
        try {
            reservaService.cancelarReserva(id);
            return "redirect:/reservas?sucesso=Reserva cancelada com sucesso";
        } catch (RuntimeException e) {
            return "redirect:/reservas?erro=" + e.getMessage().replace(" ", "%20");
        }
    }

    @GetMapping("/reservas/excluir/{id}")
    public String excluirReserva(@PathVariable String id) {
        reservaService.excluir(id);
        return "redirect:/reservas?sucesso=Reserva excluída com sucesso";
    }
}