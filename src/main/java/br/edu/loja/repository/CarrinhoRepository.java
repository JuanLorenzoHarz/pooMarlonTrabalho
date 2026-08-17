package br.edu.loja.repository;

import br.edu.loja.domain.Carrinho;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    Optional<Carrinho> findByClienteId(Long clienteId);
}
