package com.example.jean.inventarioApp.model;

import java.util.Date;

public class Item {
    private String identificadorInventario;
    private String id;
    private String nome;
    private Date data;
    private String descricao;
    private Integer Qtd;
    private String categoria;
    private String urlFoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getIdentificadorInventario() {
        return identificadorInventario;
    }

    public void setIdentificadorInventario(String identificadorInventario) {
        this.identificadorInventario = identificadorInventario;
    }

    public Integer getQtd() {
        return Qtd;
    }

    public void setQtd(Integer qtd) {
        Qtd = qtd;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
