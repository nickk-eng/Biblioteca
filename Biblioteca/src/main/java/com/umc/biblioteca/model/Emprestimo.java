package com.umc.biblioteca.model;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "emprestimos")
public class Emprestimo {

    @Id
    private String id;
    private String livroId;
    private String usuarioId;
    private String tituloLivro;
    private String nomeUsuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private String situacao;
    private double multa;

    public Emprestimo() {
    }

    public Emprestimo(String id, String livroId, String usuarioId, String tituloLivro, String nomeUsuario,
                      LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao, LocalDate dataDevolucao,
                      String situacao, double multa) {
        this.id = id;
        this.livroId = livroId;
        this.usuarioId = usuarioId;
        this.tituloLivro = tituloLivro;
        this.nomeUsuario = nomeUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucao = dataDevolucao;
        this.situacao = situacao;
        this.multa = multa;
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

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public String getSituacao() {
        return situacao;
    }

    public double getMulta() {
        return multa;
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

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }
}