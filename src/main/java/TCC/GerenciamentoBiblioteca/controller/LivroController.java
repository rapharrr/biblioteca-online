package TCC.GerenciamentoBiblioteca.controller;

import TCC.GerenciamentoBiblioteca.model.Livro;
import TCC.GerenciamentoBiblioteca.model.Usuario;
import TCC.GerenciamentoBiblioteca.repository.LivroRepository;
import TCC.GerenciamentoBiblioteca.services.LivroService;
import TCC.GerenciamentoBiblioteca.services.UsuarioService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioService usuarioService;


    // Adiciona um novo livro
    @PostMapping("/adicionar")
    public ResponseEntity<Livro> adicionarLivro(@RequestBody Livro livro) {
        Livro novoLivro = livroService.salvar(livro);
        return ResponseEntity.ok(novoLivro);
    }

    // Lista apenas os livros disponíveis
    @GetMapping
    public List<Livro> listarLivrosDisponiveis() {
        return livroRepository.findByDisponivel(true);
    }

    // Lista todos os livros (disponíveis e indisponíveis)
    @GetMapping("/todos")
    public List<Livro> listarTodosLivros() {
        return livroRepository.findAll();
    }

    // Obtém os detalhes de um livro específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Livro> obterDetalhesLivro(@PathVariable Long id) {
        Livro livro = livroService.obterLivroPorId(id);
        return livro != null ? ResponseEntity.ok(livro) : ResponseEntity.notFound().build();
    }

    // Remove um livro específico por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerLivro(@PathVariable Long id) {
        if (livroService.obterLivroPorId(id) != null) {
            livroService.remover(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/detalhes-livro/{id}")
    public String detalhesLivro(@PathVariable Long id, Model model) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        model.addAttribute("livro", livro);
        return "detalhesLivros"; // nome do arquivo .html (detalhesLivros.html)
    }

    // Endpoint para pesquisar livros
    @GetMapping("/pesquisar")
    public List<Livro> pesquisarLivros(@RequestParam String termo) {
        return livroService.pesquisarLivros(termo);
    }

    @GetMapping("/")
    public String mostrarPaginaInicial(Model model, HttpSession session) {
        boolean usuarioLogado = session.getAttribute("usuario") != null;
        model.addAttribute("usuarioLogado", usuarioLogado);

        if (usuarioLogado) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            List<String> generosPreferidos = usuarioService.getGenerosUsuario(usuario.getId().intValue());
            model.addAttribute("generosPreferidos", generosPreferidos);
        }
        return "index";
    }

}