package TCC.GerenciamentoBiblioteca.repository;

import TCC.GerenciamentoBiblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByDisponivel(boolean disponivel);


  // Busca livros por título, autor ou ambos
    @Query("SELECT l FROM Livro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Livro> findByTitleOrAuthor(String termo);

}
