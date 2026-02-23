package br.com.Biblioteca.view;

import br.com.Biblioteca.controller.SessionManager;
import br.com.Biblioteca.controller.UsuarioController;
import br.com.Biblioteca.entity.Usuario;
import br.com.Biblioteca.enums.TipoUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {
    private UsuarioController usuarioController = new UsuarioController();
    private JTextField txtEmail = new JTextField(20);
    private JPasswordField txtSenha = new JPasswordField(20);
    private JTextField txtNome = new JTextField(20); // Para cadastro

    public LoginView() {
        setTitle("Biblioteca - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        mostrarTelaLogin();
    }

    private void mostrarTelaLogin() {
        getContentPane().removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        add(txtSenha, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(this::acaoLogin);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnLogin, gbc);

        JButton btnIrCadastro = new JButton("Não tem conta? Cadastre-se");
        btnIrCadastro.addActionListener(e -> mostrarTelaCadastro());
        gbc.gridy = 3;
        add(btnIrCadastro, gbc);

        revalidate();
        repaint();
    }

    private void mostrarTelaCadastro() {
        getContentPane().removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        add(txtSenha, gbc);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(this::acaoCadastro);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnCadastrar, gbc);

        JButton btnVoltar = new JButton("Voltar para Login");
        btnVoltar.addActionListener(e -> mostrarTelaLogin());
        gbc.gridy = 4;
        add(btnVoltar, gbc);

        revalidate();
        repaint();
    }

    private void acaoLogin(ActionEvent e) {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());
        Usuario u = usuarioController.login(email, senha);
        if (u != null) {
            SessionManager.salvarSessao(u);
            abrirMainView();
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoCadastro(ActionEvent e) {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());
        
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos");
            return;
        }

        String result = usuarioController.adicionarUsuario(nome, email, senha, TipoUsuario.NORMAL);
        if (result.equals("Sucesso")) {
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
            mostrarTelaLogin();
        } else {
            JOptionPane.showMessageDialog(this, result, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirMainView() {
        new MainView().setVisible(true);
        this.dispose();
    }
}
