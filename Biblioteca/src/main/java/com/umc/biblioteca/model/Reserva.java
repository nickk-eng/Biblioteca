package com.umc.biblioteca.model;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservas")
public class Reserva {

    @Id
    private String id;
    private String livroId;
    private String usuarioId;
    private String tituloLivro;
    private String nomeUsuario;
    private LocalDate dataSolicitacao;
    private LocalDate dataExpiracao;
    private LocalDate dataAtendimento;
    private String status;

    public Reserva() {
    }

    public Reserva(String id, String livroId, String usuarioId, String tituloLivro, String nomeUsuario,
                   LocalDate dataSolicitacao, LocalDate dataExpiracao, LocalDate dataAtendimento, String status) {
        this.id = id;
        this.livroId = livroId;
        this.usuarioId = usuarioId;
        this.tituloLivro = tituloLivro;
        this.nomeUsuario = nomeUsuario;
        this.dataSolicitacao = dataSolicitacao;
        this.dataExpiracao = dataExpiracao;
        this.dataAtendimento = dataAtendimento;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getLivroId() {
        return livroId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public LocalDate getDataAtendimento() {
        return dataAtendimento;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLivroId(String livroId) {
        this.livroId = livroId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public void setDataAtendimento(LocalDate dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
