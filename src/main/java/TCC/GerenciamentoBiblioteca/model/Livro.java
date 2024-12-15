package TCC.GerenciamentoBiblioteca.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "livros") // Certifique-se de que o nome aqui está correto
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String isbn;
    private double preco;
    private String sinopse;
    private String capaUrl;
    private Double avaliacaoMedia;
    private Integer anoPublicacao;
    private String Genero;

    @Column(name = "data_publicacao") // Verifique se os nomes correspondem
    private LocalDate dataPublicacao;

    private boolean disponivel;

}
