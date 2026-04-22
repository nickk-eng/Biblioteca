package com.umc.biblioteca.service;

import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() != null && usuario.getId().isBlank()) {
            usuario.setId(null);
        }

        validarUsuario(usuario);

        if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent()
                && usuario.getId() == null) {
            throw new RuntimeException("Login já cadastrado");
        }

        if (usuario.getId() != null) {
            Usuario existente = buscarPorId(usuario.getId());

            if (!existente.getLogin().equals(usuario.getLogin())
                    && usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
                throw new RuntimeException("Login já cadastrado");
            }

            if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                usuario.setSenha(existente.getSenha());
            } else if (!usuario.getSenha().startsWith("$2a$")
                    && !usuario.getSenha().startsWith("$2b$")
                    && !usuario.getSenha().startsWith("$2y$")) {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            }

            return usuarioRepository.save(usuario);
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public void excluir(String id) {
        usuarioRepository.deleteById(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        if (usuario.getMatricula() == null || usuario.getMatricula().isBlank()) {
            throw new RuntimeException("Matrícula é obrigatória");
        }
        if (usuario.getEndereco() == null || usuario.getEndereco().isBlank()) {
            throw new RuntimeException("Endereço é obrigatório");
        }
        if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isBlank()) {
            throw new RuntimeException("Tipo de usuário é obrigatório");
        }
        if (!usuario.getTipoUsuario().equals("ALUNO")
                && !usuario.getTipoUsuario().equals("PROFESSOR")
                && !usuario.getTipoUsuario().equals("FUNCIONARIO")) {
            throw new RuntimeException("Tipo de usuário inválido");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new RuntimeException("E-mail é obrigatório");
        }
        if (usuario.getTelefone() == null || usuario.getTelefone().isBlank()) {
            throw new RuntimeException("Telefone é obrigatório");
        }
        if (usuario.getLogin() == null || usuario.getLogin().isBlank()) {
            throw new RuntimeException("Login é obrigatório");
        }
        if ((usuario.getId() == null || usuario.getId().isBlank())
                && (usuario.getSenha() == null || usuario.getSenha().isBlank())) {
            throw new RuntimeException("Senha é obrigatória");
        }
    }
}