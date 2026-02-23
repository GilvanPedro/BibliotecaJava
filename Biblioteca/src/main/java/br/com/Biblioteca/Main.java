package br.com.Biblioteca;

import br.com.Biblioteca.controller.SessionManager;
import br.com.Biblioteca.entity.Usuario;
import br.com.Biblioteca.view.LoginView;
import br.com.Biblioteca.view.MainView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Configura o visual para o do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Usuario logado = SessionManager.carregarSessao();
            if (logado != null) {
                new MainView().setVisible(true);
            } else {
                new LoginView().setVisible(true);
            }
        });
    }
}
