package br.edu.loja.repository;

import br.edu.loja.domain.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {
    Optional<CupomDesconto> findByCodigoIgnoreCase(String codigo);
    boolean existsByCodigoIgnoreCase(String codigo);
}
