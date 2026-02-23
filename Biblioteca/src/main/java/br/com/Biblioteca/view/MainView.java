package br.com.Biblioteca.view;

import br.com.Biblioteca.controller.LivroController;
import br.com.Biblioteca.controller.SessionManager;
import br.com.Biblioteca.controller.UsuarioController;
import br.com.Biblioteca.entity.Livro;
import br.com.Biblioteca.entity.Usuario;
import br.com.Biblioteca.enums.LivroCategoria;
import br.com.Biblioteca.enums.TipoUsuario;
import br.com.Biblioteca.service.GoogleBooksService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane = new JTabbedPane();
    private LivroController livroController = new LivroController();
    private UsuarioController usuarioController = new UsuarioController();
    private GoogleBooksService googleBooksService = new GoogleBooksService();
    private Usuario logado = SessionManager.getUsuarioLogado();

    public MainView() {
        setTitle("Sistema de Biblioteca - Logado como: " + logado.getNome());
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Aba Biblioteca (Busca API + Lista Local)
        tabbedPane.addTab("Biblioteca", criarPainelBiblioteca());

        // Aba CRUD Livros (Apenas para livros locais)
        tabbedPane.addTab("Gerenciar Livros", criarPainelCrudLivros());

        // Aba CRUD Usuários (Apenas para ADMIN)
        if (logado.getTipo() == TipoUsuario.ADMIN) {
            tabbedPane.addTab("Gerenciar Usuários", criarPainelCrudUsuarios());
        }

        // Botão de Sair
        JPanel pnlSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSair = new JButton("Sair da Conta");
        btnSair.addActionListener(e -> {
            SessionManager.encerrarSessao();
            new LoginView().setVisible(true);
            this.dispose();
        });
        pnlSul.add(btnSair);

        add(tabbedPane, BorderLayout.CENTER);
        add(pnlSul, BorderLayout.SOUTH);
    }

    private JPanel criarPainelBiblioteca() {
        JPanel painel = new JPanel(new BorderLayout());
        
        JPanel pnlBusca = new JPanel(new FlowLayout());
        JTextField txtBusca = new JTextField(30);
        JButton btnBuscar = new JButton("Buscar na Google Books");
        pnlBusca.add(new JLabel("Pesquisar:"));
        pnlBusca.add(txtBusca);
        pnlBusca.add(btnBuscar);

        String[] colunas = {"ID", "Título", "Autor", "Categorias", "Fonte"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        
        btnBuscar.addActionListener(e -> {
            model.setRowCount(0);
            // Primeiro busca local
            List<Livro> locais = livroController.getLivrosLista();
            for (Livro l : locais) {
                if (l.getTitulo().toLowerCase().contains(txtBusca.getText().toLowerCase())) {
                    model.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getCategoria(), "Local"});
                }
            }
            // Depois busca na API
            new Thread(() -> {
                List<Livro> apiLivros = googleBooksService.buscarLivros(txtBusca.getText());
                SwingUtilities.invokeLater(() -> {
                    for (Livro l : apiLivros) {
                        model.addRow(new Object[]{"-", l.getTitulo(), l.getAutor(), l.getCategoria(), "Google API"});
                    }
                });
            }).start();
        });

        painel.add(pnlBusca, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        return painel;
    }

    private JPanel criarPainelCrudLivros() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"ID", "Título", "Autor", "Categorias"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        
        Runnable atualizarTabela = () -> {
            model.setRowCount(0);
            for (Livro l : livroController.getLivrosLista()) {
                model.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getCategoria()});
            }
        };
        atualizarTabela.run();

        JPanel pnlForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTitulo = new JTextField(20);
        JTextField txtAutor = new JTextField(20);
        
        // JList para múltiplas categorias
        JList<LivroCategoria> listCategorias = new JList<>(LivroCategoria.values());
        listCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollCategorias = new JScrollPane(listCategorias);
        scrollCategorias.setPreferredSize(new Dimension(200, 80));

        JButton btnAdd = new JButton("Adicionar Livro Local");
        JButton btnRemover = new JButton("Remover Selecionado");

        gbc.gridx = 0; gbc.gridy = 0; pnlForm.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtTitulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; pnlForm.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; pnlForm.add(txtAutor, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; pnlForm.add(new JLabel("Categorias (Segure Ctrl):"), gbc);
        gbc.gridx = 1; pnlForm.add(scrollCategorias, gbc);
        
        JPanel pnlBotoes = new JPanel(new FlowLayout());
        pnlBotoes.add(btnAdd);
        pnlBotoes.add(btnRemover);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        pnlForm.add(pnlBotoes, gbc);

        btnAdd.addActionListener(e -> {
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();
            List<LivroCategoria> selecionadas = listCategorias.getSelectedValuesList();
            
            if (titulo.isEmpty() || selecionadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Título e pelo menos uma categoria são obrigatórios.");
                return;
            }

            Set<LivroCategoria> categoriaSet = new HashSet<>(selecionadas);
            livroController.adicionarLivro(titulo, autor, false, categoriaSet);
            atualizarTabela.run();
            
            // Limpar campos
            txtTitulo.setText("");
            txtAutor.setText("");
            listCategorias.clearSelection();
        });

        btnRemover.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                long id = (long) model.getValueAt(row, 0);
                livroController.removerLivro(id);
                atualizarTabela.run();
            }
        });

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(pnlForm, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarPainelCrudUsuarios() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"ID", "Nome", "Email", "Tipo"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);

        Runnable atualizarTabela = () -> {
            model.setRowCount(0);
            for (Usuario u : usuarioController.getUsuariosLista()) {
                model.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getTipo()});
            }
        };
        atualizarTabela.run();

        JPanel pnlAcoes = new JPanel(new FlowLayout());
        JButton btnRemover = new JButton("Remover Usuário");
        btnRemover.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                long id = (long) model.getValueAt(row, 0);
                if (id == logado.getId()) {
                    JOptionPane.showMessageDialog(this, "Você não pode se remover!");
                    return;
                }
                usuarioController.removerUsuario(id);
                atualizarTabela.run();
            }
        });
        pnlAcoes.add(btnRemover);

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(pnlAcoes, BorderLayout.SOUTH);
        return painel;
    }
}
