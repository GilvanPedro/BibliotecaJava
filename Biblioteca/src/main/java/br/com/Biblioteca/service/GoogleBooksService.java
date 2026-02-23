package br.com.Biblioteca.service;

import br.com.Biblioteca.entity.Livro;
import br.com.Biblioteca.enums.LivroCategoria;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoogleBooksService {

    public List<Livro> buscarLivros(String query) {
        List<Livro> livros = new ArrayList<>();
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&maxResults=10");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray items = jsonResponse.getAsJsonArray("items");

                if (items != null) {
                    for (JsonElement element : items) {
                        JsonObject volumeInfo = element.getAsJsonObject().getAsJsonObject("volumeInfo");
                        
                        String titulo = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Sem título";
                        
                        String autor = "Desconhecido";
                        if (volumeInfo.has("authors")) {
                            autor = volumeInfo.getAsJsonArray("authors").get(0).getAsString();
                        }

                        Set<LivroCategoria> categorias = new HashSet<>();
                        if (volumeInfo.has("categories")) {
                            String catStr = volumeInfo.getAsJsonArray("categories").get(0).getAsString().toUpperCase();
                            for (LivroCategoria lc : LivroCategoria.values()) {
                                if (catStr.contains(lc.name())) {
                                    categorias.add(lc);
                                }
                            }
                        }
                        if (categorias.isEmpty()) categorias.add(LivroCategoria.DIDATICO);

                        // ID temporário para exibição (não persistido)
                        livros.add(new Livro(0, titulo, autor, false, categorias));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return livros;
    }
}
