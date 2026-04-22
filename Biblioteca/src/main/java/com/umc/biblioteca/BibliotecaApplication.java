package com.umc.biblioteca;

import com.umc.biblioteca.model.Usuario;
import com.umc.biblioteca.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner criarAdminPadrao(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByLogin("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setMatricula("ADMIN001");
                admin.setEndereco("Biblioteca Central UMC");
                admin.setTipoUsuario("FUNCIONARIO");
                admin.setEmail("admin@biblioteca.com");
                admin.setTelefone("(00)00000-0000");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin123"));
                usuarioRepository.save(admin);
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Usuario usuario = usuarioRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            String role = "ALUNO";

            if ("FUNCIONARIO".equals(usuario.getTipoUsuario())) {
                role = "FUNCIONARIO";
            } else if ("PROFESSOR".equals(usuario.getTipoUsuario())) {
                role = "PROFESSOR";
            }

            UserDetails userDetails = User.withUsername(usuario.getLogin())
                    .password(usuario.getSenha())
                    .roles(role)
                    .build();

            return userDetails;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/login",
                        "/cadastro",
                        "/cadastro/salvar",
                        "/acesso-negado",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll()
                .requestMatchers("/admin", "/livros/**", "/usuarios/**", "/emprestimos/**", "/reservas/**")
                    .hasRole("FUNCIONARIO")
                .requestMatchers("/meus-emprestimos/**", "/minhas-reservas/**", "/historico/**")
                    .hasAnyRole("ALUNO", "PROFESSOR", "FUNCIONARIO")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    boolean funcionario = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_FUNCIONARIO"));

                    if (funcionario) {
                        response.sendRedirect("/admin");
                    } else {
                        response.sendRedirect("/meus-emprestimos");
                    }
                })
                .failureUrl("/login?erro=true")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/acesso-negado")
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}