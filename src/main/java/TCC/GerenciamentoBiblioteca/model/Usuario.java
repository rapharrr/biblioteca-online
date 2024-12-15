package TCC.GerenciamentoBiblioteca.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String generoPreferido1;
    private String generoPreferido2;
    private String generoPreferido3;

    // Método para obter os gêneros preferidos como lista
    public List<String> getGenerosPreferidos() {
        List<String> generos = new ArrayList<>();
        if (generoPreferido1 != null && !generoPreferido1.isEmpty())
            generos.add(generoPreferido1);
        if (generoPreferido2 != null && !generoPreferido2.isEmpty())
            generos.add(generoPreferido2);
        if (generoPreferido3 != null && !generoPreferido3.isEmpty())
            generos.add(generoPreferido3);
        return generos;
    }
}
