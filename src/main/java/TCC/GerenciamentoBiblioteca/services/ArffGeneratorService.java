package TCC.GerenciamentoBiblioteca.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TCC.GerenciamentoBiblioteca.model.Livro;
import TCC.GerenciamentoBiblioteca.repository.LivroRepository;

@Service
public class ArffGeneratorService {

    @Autowired
    private LivroRepository livroRepository;

    private final String WEKA_DIR = "src/main/resources/weka";
    private final String ARFF_FILE_NAME = "livros.arff";

    public String gerarArquivoArff() throws IOException {
        // Garante que o diretório 'resources/weka' exista
        Path wekaPath = Paths.get(WEKA_DIR);
        if (!Files.exists(wekaPath)) {
            Files.createDirectories(wekaPath); // Cria o diretório se não existir
        }

        // Caminho completo do arquivo
        File arffFile = new File(wekaPath.toFile(), ARFF_FILE_NAME);

        // Obtemos os dados do banco
        List<Livro> livros = livroRepository.findAll();

        try (FileWriter writer = new FileWriter(arffFile)) {
            // Cabeçalho do arquivo .arff
            writer.write("@relation livros\n\n");
            writer.write("@attribute id numeric\n");
            writer.write("@attribute titulo string\n");
            writer.write("@attribute genero {Fantasy, Science_Fiction, Romance, Mystery, Thriller, Adventure, Biography, History, Children, Horror, Classic, Fiction, Drama, Comedy}\n");
            writer.write("@attribute avaliacao_media numeric\n");
            writer.write("@attribute preco numeric\n");
            writer.write("@attribute classe {1, 0}\n\n");
            writer.write("@data\n");

            // Dados do banco
            for (Livro livro : livros) {
                String linha = String.format("%d, \"%s\", %s, %.1f, %.2f, %d\n",
                        livro.getId(),
                        livro.getTitulo().replaceAll("\"", ""), // Remove aspas no título
                        livro.getGenero().replace(" ", "_"), // Substitui espaços em branco no gênero por _
                        livro.getAvaliacaoMedia(),
                        livro.getPreco(),
                        1 // Classe padrão, ajustável para lógica de treinamento
                );
                writer.write(linha);
            }
        }

        return arffFile.getAbsolutePath(); // Retorna o caminho completo do arquivo gerado
    }
}
