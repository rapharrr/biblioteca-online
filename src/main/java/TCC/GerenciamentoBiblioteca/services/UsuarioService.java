package TCC.GerenciamentoBiblioteca.services;

import TCC.GerenciamentoBiblioteca.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import TCC.GerenciamentoBiblioteca.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

     // Salva o usuário básico (sem gêneros inicialmente)
     public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void salvarGenerosParaUsuario(Usuario usuario, List<String> generos) {
        if (generos.size() == 3) {
            usuario.setGeneroPreferido1(generos.get(0));
            usuario.setGeneroPreferido2(generos.get(1));
            usuario.setGeneroPreferido3(generos.get(2));
            usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("O usuário deve escolher exatamente 3 gêneros.");
        }
    }

    public List<String> getGenerosUsuario(int usuarioId) {
        Usuario usuario = usuarioRepository.findById((long) usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        List<String> generos = new ArrayList<>();
        generos.add(usuario.getGeneroPreferido1());
        generos.add(usuario.getGeneroPreferido2());
        generos.add(usuario.getGeneroPreferido3());
        return generos;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado, por favor utilize outro e-mail.");
        }
        return usuarioRepository.save(usuario);
    }
}
