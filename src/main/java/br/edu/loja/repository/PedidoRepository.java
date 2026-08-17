package br.edu.loja.repository;

import br.edu.loja.domain.Pedido;
import br.edu.loja.domain.StatusPedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Override
    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto", "itens.produto.categoria"})
    Optional<Pedido> findById(Long id);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto", "itens.produto.categoria"})
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(Long clienteId);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto", "itens.produto.categoria"})
    List<Pedido> findAllByOrderByCriadoEmDesc();

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto", "itens.produto.categoria"})
    List<Pedido> findByStatusInOrderByCriadoEmDesc(List<StatusPedido> status);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto", "itens.produto.categoria"})
    List<Pedido> findByStatusInAndCriadoEmBetweenOrderByCriadoEmDesc(List<StatusPedido> status, LocalDateTime inicio, LocalDateTime fim);
}
