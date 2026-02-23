package br.com.Biblioteca.controller;

import br.com.Biblioteca.entity.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class SessionManager {
    private static final String SESSION_FILE = System.getProperty("user.dir") + "/Arquivos/sessao.json";
    private static Usuario usuarioLogado;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void salvarSessao(Usuario usuario) {
        usuarioLogado = usuario;
        try (FileWriter writer = new FileWriter(SESSION_FILE)) {
            gson.toJson(usuario, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Usuario carregarSessao() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file)) {
            usuarioLogado = gson.fromJson(reader, Usuario.class);
            return usuarioLogado;
        } catch (IOException e) {
            return null;
        }
    }

    public static void encerrarSessao() {
        usuarioLogado = null;
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
