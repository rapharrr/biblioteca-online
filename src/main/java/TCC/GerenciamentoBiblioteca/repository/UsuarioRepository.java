package TCC.GerenciamentoBiblioteca.repository;

import TCC.GerenciamentoBiblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    Usuario findByNome(String nome);
    Usuario findByEmailAndSenha(String email, String senha);
    boolean existsByEmail(String email);
    
}
