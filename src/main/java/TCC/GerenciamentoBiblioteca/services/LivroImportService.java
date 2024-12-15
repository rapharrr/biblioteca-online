package TCC.GerenciamentoBiblioteca.services;

import TCC.GerenciamentoBiblioteca.model.Livro;
import TCC.GerenciamentoBiblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LivroImportService {

    @Autowired
    private LivroRepository livroRepository;

    private final String OPEN_LIBRARY_API_URL = "https://openlibrary.org/search.json";

    private static final List<String> generosPrincipais = Arrays.asList(
            "Fantasy", "Science Fiction", "Romance", "Mystery", "Thriller", "Adventure", "Biography",
            "History", "Children", "Horror", "Classic", "Fiction", "Drama", "Comedy");

    // Método auxiliar para obter a URL da capa do livro
    private String getCoverUrl(Map<String, Object> livroData) {
        Object coverId = livroData.get("cover_i");
        if (coverId != null) {
            return "https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg";
        }
        return null;
    }

    // Método para filtrar gêneros com base em palavras-chave principais
    private String filtrarGeneros(List<String> subjects) {
        // Lista para armazenar os gêneros relevantes
        List<String> generosRelevantes = new ArrayList<>();

        for (String subject : subjects) {
            for (String genero : generosPrincipais) {
                if (subject.toLowerCase().contains(genero.toLowerCase())) {
                    generosRelevantes.add(genero);
                    break; // Evita duplicar gêneros já encontrados
                }
            }
        }

        // Remove duplicatas e junta os gêneros com vírgulas
        return generosRelevantes.stream().distinct().collect(Collectors.joining(", "));
    }

    // Importar livros por título (quantidade ilimitada)
    public void importarLivrosPorTitulo(String titulo) {
        String url = UriComponentsBuilder.fromHttpUrl(OPEN_LIBRARY_API_URL)
                .queryParam("title", titulo)
                .toUriString();

        importarLivros(url, Integer.MAX_VALUE); // Sem limite para títulos
    }

    // Importar livros por gênero (limitado a 30 livros)
    public void importarLivrosPorGenero(String genero) {
        String url = UriComponentsBuilder.fromHttpUrl(OPEN_LIBRARY_API_URL)
                .queryParam("subject", genero)
                .toUriString();

        importarLivros(url, 30); // Limite de até 30 livros
    }

    // Método genérico para importar livros com limite
    private void importarLivros(String url, int maxLivros) {
        RestTemplate restTemplate = new RestTemplate();

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> livros = (List<Map<String, Object>>) response.get("docs");

            if (livros != null) {
                int contador = 0;
                for (Map<String, Object> livroData : livros) {
                    if (contador >= maxLivros)
                        break;

                    Livro livro = new Livro();

                    // Configurar título
                    livro.setTitulo((String) livroData.get("title"));

                    // Tratar ausência de autor
                    @SuppressWarnings("unchecked")
                    List<String> autores = (List<String>) livroData.get("author_name");
                    if (autores != null && !autores.isEmpty()) {
                        livro.setAutor(autores.get(0));
                    } else {
                        System.out.println("Livro ignorado devido à ausência de autor: " + livroData.get("title"));
                        continue;
                    }

                    // Tratar ausência de capa
                    String capaUrl = getCoverUrl(livroData);
                    if (capaUrl == null) {
                        System.out.println("Livro ignorado devido à ausência de capa: " + livroData.get("title"));
                        continue;
                    }
                    livro.setCapaUrl(capaUrl);

                    // Adicionar gêneros (subject) ao livro
                    @SuppressWarnings("unchecked")
                    List<String> subjects = (List<String>) livroData.get("subject");
                    if (subjects != null && !subjects.isEmpty()) {
                        // Filtra os gêneros com base nas palavras-chave principais
                        String generos = filtrarGeneros(subjects);

                        // Define "Sem gênero especificado" caso nenhum gênero relevante seja encontrado
                        livro.setGenero(generos.isEmpty() ? "Sem gênero especificado" : generos);
                    } else {
                        livro.setGenero("Sem gênero especificado");
                    }
                    livro.setPreco(29.90);
                    livro.setSinopse("Sinopse do livro");
                    livro.setAvaliacaoMedia(4.5);
                    livro.setAnoPublicacao(2023);
                    livro.setDisponivel(true);

                    // Salvar no banco
                    livroRepository.save(livro);
                    contador++;
                }
                System.out.println("Importação concluída: " + contador + " livros importados.");
            } else {
                System.out.println("Nenhum livro encontrado para a URL fornecida.");
            }
        } else {
            System.out.println("Erro ao obter dados da API do Open Library.");
        }
    }
}
