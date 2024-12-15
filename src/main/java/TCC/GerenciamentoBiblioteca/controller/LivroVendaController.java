package TCC.GerenciamentoBiblioteca.controller;

import TCC.GerenciamentoBiblioteca.model.Livro;
import TCC.GerenciamentoBiblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/livros-venda")
public class LivroVendaController {
    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public List<Livro> listarLivrosVenda() {
        return livroRepository.findByDisponivel(true); // Filtra os livros disponíveis
    }
}
