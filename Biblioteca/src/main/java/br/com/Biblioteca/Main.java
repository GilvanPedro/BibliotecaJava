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
                "Uzumaki",
                "Junji Ito",
                false,
                Set.of(LivroCategoria.TERROR, LivroCategoria.SUSPENSE, LivroCategoria.DRAMA)
        );

        livroController.exibirLivro();

        usuarioController.adicionarUsuario(
                "Rogério",
                "Rogériomarceneiro@gmail.com",
                "gilllll",
                TipoUsuario.ADMIN);

        usuarioController.editarUsuario(
                3,
                "Rogério",
                "Rogériomarceneiro@gmail.com",
                "rogerinhopoggers",
                TipoUsuario.NORMAL);

        usuarioController.exibirUsuarios();
    }
}