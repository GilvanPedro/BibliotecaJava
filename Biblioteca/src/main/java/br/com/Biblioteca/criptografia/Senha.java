package br.com.Biblioteca.criptografia;

import org.mindrot.jbcrypt.BCrypt;

public class Senha {
    public String CriptografarSenha(String senha){
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public boolean VerificarSenha(String senha, String hash){
        return BCrypt.checkpw(senha, hash);
    }
}
