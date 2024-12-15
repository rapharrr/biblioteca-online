package TCC.GerenciamentoBiblioteca.controller;

import TCC.GerenciamentoBiblioteca.services.LivroImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/importar-livros")
public class LivroImportController {

    @Autowired
    private LivroImportService livroImportService;

    // Endpoint para importar livros por título
    @GetMapping("/titulo")
    public String importarLivrosPorTitulo(@RequestParam String titulo) {
        try {
            livroImportService.importarLivrosPorTitulo(titulo);
            return "Importação de livros com título '" + titulo + "' concluída.";
        } catch (Exception e) {
            return "Erro ao importar livros por título: " + e.getMessage();
        }
    }

    // Endpoint para importar livros por gênero
    @GetMapping("/genero")
    public String importarLivrosPorGenero(@RequestParam String genero) {
        try {
            livroImportService.importarLivrosPorGenero(genero);
            return "Importação de livros do gênero '" + genero + "' concluída.";
        } catch (Exception e) {
            return "Erro ao importar livros por gênero: " + e.getMessage();
        }
    }

    // Endpoint para popular o banco com vários gêneros e títulos
    @GetMapping("/popular")
    public String popularBanco() {
        // Lista de gêneros
        List<String> generos = Arrays.asList(
                "Fantasy", "Science Fiction", "Mystery", "Romance", "Adventure",
                "Biography", "History", "Thriller", "Children", "Horror");

        // Lista de títulos
        List<String> titulos = Arrays.asList(
                "Harry Potter", "The Lord of the Rings", "1984",
                "The Great Gatsby", "Pride and Prejudice",
                "To Kill a Mockingbird", "The Catcher in the Rye",
                "Moby Dick", "Crime and Punishment", "War and Peace");

        StringBuilder log = new StringBuilder();

        // Importar por gêneros
        for (String genero : generos) {
            try {
                livroImportService.importarLivrosPorGenero(genero);
                log.append("Gênero importado com sucesso: ").append(genero).append("\n");
            } catch (Exception e) {
                log.append("Erro ao importar gênero ").append(genero).append(": ").append(e.getMessage()).append("\n");
            }
        }

        // Importar por títulos
        for (String titulo : titulos) {
            try {
                livroImportService.importarLivrosPorTitulo(titulo);
                log.append("Título importado com sucesso: ").append(titulo).append("\n");
            } catch (Exception e) {
                log.append("Erro ao importar título ").append(titulo).append(": ").append(e.getMessage()).append("\n");
            }
        }

        return log.toString();
    }
}
