package TCC.GerenciamentoBiblioteca.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import TCC.GerenciamentoBiblioteca.model.Livro;
import TCC.GerenciamentoBiblioteca.repository.LivroRepository;
import TCC.GerenciamentoBiblioteca.services.RecomendacaoService;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoController {

    @Autowired
    private RecomendacaoService recomendacaoService;

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public List<Livro> obterRecomendacoes(@RequestParam List<String> generosFavoritos) {
        // Gere o caminho do arquivo ARFF
        String caminhoARFF = recomendacaoService.gerarArquivoARFF();

        // Obtenha os IDs recomendados
        List<Integer> idsRecomendados = recomendacaoService.recomendarLivros(caminhoARFF, generosFavoritos);

        // Converta IDs para Long
        List<Long> idsRecomendadosLong = idsRecomendados.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());

        // Retorne os livros correspondentes
        return livroRepository.findAllById(idsRecomendadosLong);
    }
}
