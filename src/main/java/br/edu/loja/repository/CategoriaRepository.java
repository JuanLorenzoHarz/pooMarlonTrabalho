package br.edu.loja.repository;

import br.edu.loja.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNomeIgnoreCase(String nome);
    List<Categoria> findByAtivaTrueOrderByNomeAsc();
}
