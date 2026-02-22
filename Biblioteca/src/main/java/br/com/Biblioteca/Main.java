package br.com.Biblioteca;

import br.com.Biblioteca.controller.LivroController;
import br.com.Biblioteca.controller.UsuarioController;
import br.com.Biblioteca.entity.Livro;
import br.com.Biblioteca.enums.LivroCategoria;
import br.com.Biblioteca.enums.TipoUsuario;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        LivroController livroController = new LivroController();
        UsuarioController usuarioController = new UsuarioController();

        livroController.adicionarLivro(
                "Pedacinhos",
                "Desconhecido",
                true,
                Set.of(LivroCategoria.TERROR, LivroCategoria.SUSPENSE, LivroCategoria.DRAMA, LivroCategoria.ADULTO)
        );

        livroController.exibirLivro();

        usuarioController.adicionarUsuario(
                "Rogério",
                "Rogériomarceneiro@gmail.com",
                "gilllll",
                TipoUsuario.ADMIN);

        usuarioController.editarUsuario(
                1,
                "Gilvan Pedro",
                "gilvanpedro2006@gmail.com",
                "gilvanpedro1234");

        usuarioController.exibirUsuarios();
    }
}