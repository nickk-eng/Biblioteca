package com.umc.biblioteca.controller;

import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model,
                                 @RequestParam(value = "erro", required = false) String erro,
                                 @RequestParam(value = "sucesso", required = false) String sucesso) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("erro", erro);
        model.addAttribute("sucesso", sucesso);
        return "usuarios";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute Usuario usuario) {
        try {
            usuarioService.salvar(usuario);
            return "redirect:/usuarios?sucesso=Usuário salvo com sucesso";
        } catch (RuntimeException e) {
            return "redirect:/usuarios?erro=" + e.getMessage().replace(" ", "%20");
        }
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable String id, Model model,
                                @RequestParam(value = "erro", required = false) String erro,
                                @RequestParam(value = "sucesso", required = false) String sucesso) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        model.addAttribute("erro", erro);
        model.addAttribute("sucesso", sucesso);
        return "usuarios";
    }

    @PostMapping("/excluir")
    public String excluirUsuario(@RequestParam String id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        if ("admin".equals(usuario.getLogin())) {
            return "redirect:/usuarios?erro=O usuário admin não pode ser excluído";
        }

        usuarioService.excluir(id);
        return "redirect:/usuarios?sucesso=Usuário excluído com sucesso";
    }
}