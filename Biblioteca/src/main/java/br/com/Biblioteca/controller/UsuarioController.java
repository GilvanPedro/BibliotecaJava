package br.com.Biblioteca.controller;

import br.com.Biblioteca.entity.Usuario;
import br.com.Biblioteca.enums.TipoUsuario;
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

public class UsuarioController {

    private final String ARQUIVO = System.getProperty("user.dir") + "/Arquivos/usuarios.json";

    List<Usuario> usuariosLista = new ArrayList<>();
    Usuario usuario;
    File file;
    FileWriter escrita;

    Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .create();

    // ================= SALVAR =================
    private void salvarArquivo(){
        try{
            File file = new File(ARQUIVO);
            file.getParentFile().mkdirs();

            escrita = new FileWriter(file);
            gson.toJson(usuariosLista, escrita);
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
                usuariosLista = new ArrayList<>();
                return;
            }

            FileReader reader = new FileReader(file);
            Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
            usuariosLista = gson.fromJson(reader, tipoLista);

            if (usuariosLista == null) {
                usuariosLista = new ArrayList<>();
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= GERAR ID =================
    private long gerarID(){
        long maior = 0;

        for(Usuario u : usuariosLista){
            if(u.getId() > maior){
                maior = u.getId();
            }
        }

        return maior + 1;
    }

    // ================= ADICIONAR =================
    public String adicionarUsuario(String nome, String email, String senha, TipoUsuario tipo){
        lerArquivo();

        long id = gerarID();

        // valida email único
        for(Usuario u : usuariosLista){
            String emailLower = email.toLowerCase();

            if(!(emailLower.endsWith("@gmail.com") ||
                    emailLower.endsWith("@yahoo.com") ||
                    emailLower.endsWith("@outlook.com"))){

                System.out.println("Email inválido");
                return "Email inválido";
            }
            if(u.getEmail().equalsIgnoreCase(email)){
                System.out.println("Usuário já cadastrado");
                return "Usuário já cadastrado";
            }
        }

        usuario = new Usuario(id, nome, email, senha, tipo);

        usuariosLista.add(usuario);
        salvarArquivo();

        return "Sucesso";
    }

    // ================= EDITAR =================
    public String editarUsuario(long id, String nome, String email, String senha, TipoUsuario tipo){
        lerArquivo();

        for(Usuario u : usuariosLista){
            if(u.getId() == id){
                u.setNome(nome);
                u.setEmail(email);
                u.setSenha(senha);
                u.setTipo(tipo);

                salvarArquivo();
                return "Usuário editado com sucesso";
            }
        }

        return "Usuário não encontrado";
    }

    // ================= EXIBIR =================
    public void exibirUsuarios(){
        lerArquivo();

        if(usuariosLista.isEmpty()){
            System.out.println("Nenhum usuário cadastrado");
            return;
        }

        for(Usuario u : usuariosLista){
            System.out.println(u);
        }
    }

    // ================= GET LISTA =================
    public List<Usuario> getUsuariosLista(){
        lerArquivo();
        return usuariosLista;
    }

    // ================= REMOVER =================
    public void removerUsuario(long id){
        lerArquivo();

        boolean removido = false;

        for(int i = 0; i < usuariosLista.size(); i++){
            if(usuariosLista.get(i).getId() == id){
                usuariosLista.remove(i);
                removido = true;
                break;
            }
        }

        if(removido){
            salvarArquivo();
            System.out.println("Usuário removido com sucesso.");
        }else{
            System.out.println("Usuário não encontrado.");
        }
    }
}