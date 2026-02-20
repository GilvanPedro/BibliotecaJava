package br.com.Biblioteca.entity;

import br.com.Biblioteca.enums.LivroCategoria;

import java.util.Set;

public class Livro {
    private long id;
    private String titulo;
    private String autor;
    private boolean reservado;
    private Set<LivroCategoria> categoria;

    public Livro(long id, String titulo, String autor, boolean reservado, Set<LivroCategoria> categoria){
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.reservado = reservado;
        this.categoria = categoria;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isReservado() {
        return reservado;
    }

    public Set<LivroCategoria> getCategoria() {
        return categoria;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setCategoria(Set<LivroCategoria> categoria) {
        this.categoria = categoria;
    }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }

    @Override
    public String toString() {
        return String.format(
                "Livro [id=%d, titulo=%s, autor=%s, reservado=%s, categoria=%s]",
                id, titulo, autor, reservado, categoria
        );
    }
}
