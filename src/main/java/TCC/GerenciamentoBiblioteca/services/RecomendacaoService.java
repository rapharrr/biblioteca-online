package TCC.GerenciamentoBiblioteca.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TCC.GerenciamentoBiblioteca.model.Livro;
import TCC.GerenciamentoBiblioteca.repository.LivroRepository;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@Service
public class RecomendacaoService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Integer> recomendarLivros(String caminhoArquivoARFF, List<String> generosFavoritos) {
        try {
            // Carregar o dataset ARFF
            DataSource source = new DataSource(caminhoArquivoARFF);
            Instances dataset = source.getDataSet();
            dataset.setClassIndex(dataset.numAttributes() - 1); // Última coluna como classe

            // Atualizar dataset para marcar "sim" ou "nao" com base nos gêneros favoritos
            for (int i = 0; i < dataset.numInstances(); i++) {
                Instance instance = dataset.instance(i);
                String genero = instance.stringValue(1); // Coluna "genero"

                // Verifica se o gênero é favorito
                if (generosFavoritos.contains(genero)) {
                    instance.setClassValue("sim");
                } else {
                    instance.setClassValue("nao");
                }
            }

            // Treinar modelo J48
            J48 arvore = new J48();
            arvore.buildClassifier(dataset);

            // Fazer previsões
            List<Integer> livrosRecomendados = new ArrayList<>();
            for (int i = 0; i < dataset.numInstances(); i++) {
                Instance instance = dataset.instance(i);
                double classePrevista = arvore.classifyInstance(instance);

                // Adicionar IDs dos livros classificados como "sim"
                if (classePrevista == dataset.classAttribute().indexOfValue("sim")) {
                    int idLivro = (int) instance.value(0); // ID do livro
                    livrosRecomendados.add(idLivro);
                }
            }

            return livrosRecomendados;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Retorna uma lista vazia em caso de erro
        }
    }

    public String gerarArquivoARFF() {
        List<Livro> livros = livroRepository.findAll();

        // Extraia todos os gêneros únicos
        Set<String> generosUnicos = livros.stream()
                .flatMap(livro -> List.of(livro.getGenero().split(",")).stream().map(String::trim))
                .collect(Collectors.toSet());

        // Gere os valores nominais únicos para o atributo "genero"
        String valoresNominais = generosUnicos.stream().distinct().collect(Collectors.joining(","));

        StringBuilder arffContent = new StringBuilder();
        arffContent.append("@relation livros\n\n");
        arffContent.append("@attribute id NUMERIC\n");
        arffContent.append("@attribute genero {" + valoresNominais + "}\n");
        arffContent.append("@attribute usuario_favorito {sim, nao}\n\n");
        arffContent.append("@data\n");

        for (Livro livro : livros) {
            // Pegue o primeiro gênero (ou um valor padrão, caso não haja)
            String[] generosLivro = livro.getGenero().split(",");
            String genero = generosLivro.length > 0 ? generosLivro[0].trim() : "Desconhecido";
            arffContent.append(String.format("%d, %s, ?\n", livro.getId(), genero));
        }

        String caminhoArquivo = "dataset.arff";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write(arffContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return caminhoArquivo;
    }
}
