package TCC.GerenciamentoBiblioteca.controller;

import TCC.GerenciamentoBiblioteca.model.Usuario;
import TCC.GerenciamentoBiblioteca.repository.UsuarioRepository;
import TCC.GerenciamentoBiblioteca.services.UsuarioService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login"; // Retorna a página de login
    }

    @PostMapping("/login")
    public String loginUsuario(@RequestParam String email, @RequestParam String senha, Model model, HttpSession session) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", usuario);
            model.addAttribute("usuarioLogado", true); // Passa a informação para o frontend
            return "redirect:/index.html?login=sucesso";
        } else {
            model.addAttribute("error", "Email ou senha inválidos.");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarPaginaRegistro() {
        return "registro";
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@RequestParam String nome, @RequestParam String email, @RequestParam String senha, Model model, HttpSession session) {
        try {
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                model.addAttribute("error", "Todos os campos são obrigatórios.");
                return "registro";
            }

            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setSenha(senha);

            Usuario usuarioSalvo = usuarioService.salvarUsuario(novoUsuario);
            session.setAttribute("usuarioId", usuarioSalvo.getId());

            return "redirect:/selecionar-generos";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao registrar usuário: " + e.getMessage());
            return "registro";
        }
    }

    @GetMapping("/selecionar-generos")
    public String mostrarSelecaoGeneros() {
        return "selecionar-generos";
    }

    @PostMapping("/usuario/salvar-generos")
    public String salvarGeneros(@RequestParam List<String> generos, HttpSession session, Model model) {
        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                model.addAttribute("erro", "Nenhum usuário encontrado. Por favor, registre-se novamente.");
                return "redirect:/registro.html";
            }

            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            usuarioService.salvarGenerosParaUsuario(usuario, generos);

            session.removeAttribute("usuarioId");

            return "redirect:/login.html";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao salvar os gêneros. Por favor, tente novamente.");
            return "selecionar-generos";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
