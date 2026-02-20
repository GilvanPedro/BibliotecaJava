package br.com.Biblioteca.controller;

import br.com.Biblioteca.entity.Livro;
import br.com.Biblioteca.enums.LivroCategoria;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LivroController {
    private final String ARQUIVO = System.getProperty("user.dir") + "/Arquivos/livros.json";

    List<Livro> livrosLista = new ArrayList<>();
    Livro livro;
    File file;
    FileWriter escrita;

    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().create();

    // ================= SALVAR =================
    private void salvarArquivo(){
        try{
            File file = new File(ARQUIVO);

            file.getParentFile().mkdirs();

            escrita = new FileWriter(file);
            gson.toJson(livrosLista, escrita);
            escrita.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // ================= LER =================
    public void lerArquivo(){
        try{
            file = new File(ARQUIVO);

            if(!file.exists()){
                livrosLista = new ArrayList<>();
                return;
            }

            FileReader reader = new FileReader(file);
            Type tipoLista = new TypeToken<List<Livro>>() {}.getType();
            livrosLista = gson.fromJson(reader, tipoLista);

            if (livrosLista == null) {
                livrosLista = new ArrayList<>();
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= GERAR ID =================
    private long gerarID(){
        long maior = 0;

        for(Livro l : livrosLista){
            if(l.getId() > maior){
                maior = l.getId();
            }
        }

        return maior + 1;
    }

    // ================= ADICIONAR =================
    public String adicionarLivro(String titulo, String autor, boolean reservado, Set<LivroCategoria> categoria){
        lerArquivo();

        long id = gerarID();

        for(Livro l : livrosLista){
            if(l.getTitulo().equalsIgnoreCase(titulo)){
                System.out.printf("Livro já cadastrado");
                return "Livro já cadastrado";
            }
        }

        livro = new Livro(
                id,
                titulo,
                autor,
                reservado,
                categoria
        );

        livrosLista.add(livro);
        salvarArquivo();

        return "Sucesso";
    }

    // ================= EDITAR =================
    public String editarLivro(long id, String titulo, String autor, Set<LivroCategoria> categoria){
        lerArquivo();

        for(int i = 0; i < livrosLista.size(); i++){
            Livro l = livrosLista.get(i);

            if(l.getId() == id){
                l.setTitulo(titulo);
                l.setAutor(autor);
                l.setCategoria(categoria);

                salvarArquivo();
                return "Livro editado com sucesso";
            }
        }

        return "Livro não encontrado";
    }

    // ================= EXIBIR =================
    public void exibirLivro(){
        lerArquivo();

        if(livrosLista.isEmpty()){
            System.out.printf("Nenhum livro cadastrado");
            return;
        }

        for(Livro l : livrosLista){
            System.out.println(l);
        }
    }

    // ================= GET LISTA =================
    public List<Livro> getLivrosLista() {
        lerArquivo();
        return livrosLista;
    }

    // ================= REMOVER =================
    public void removerLivro(long id) {
        lerArquivo();

        boolean removido = false;

        for (int i = 0; i < livrosLista.size(); i++) {
            if (livrosLista.get(i).getId() == id) {
                livrosLista.remove(i);
                removido = true;
                break;
            }
        }

        if (removido) {
            salvarArquivo();
            System.out.println("Livro removido com sucesso.");
        } else {
            System.out.println("Livro não encontrado.");
        }
    }
}
