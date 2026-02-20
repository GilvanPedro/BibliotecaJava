package br.com.Biblioteca.entity;

import br.com.Biblioteca.enums.TipoUsuario;

public class Usuario {
    private long id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipo;

    public Usuario(long id, String nome, String email, String senha, TipoUsuario tipo){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return String.format(
                "Usuario [id=%d, nome='%s', email='%s', tipo=%s]",
                id, nome, email, tipo
        );
    }
}
