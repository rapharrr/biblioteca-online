package TCC.GerenciamentoBiblioteca.services;

import TCC.GerenciamentoBiblioteca.model.Livro;
import TCC.GerenciamentoBiblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    public Livro adicionarLivro(Livro livro) {
        return livroRepository.save(livro);
    }
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }
    public Livro obterLivroPorId(Long id) {
        return livroRepository.findById(id).orElse(null);
    }

    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    public void remover(Long id) {
        livroRepository.deleteById(id);
    }

    public List<Livro> pesquisarLivros(String termo) {
        return livroRepository.findByTitleOrAuthor(termo);
    }
}