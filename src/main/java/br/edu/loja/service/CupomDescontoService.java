package br.edu.loja.service;

import br.edu.loja.domain.*;
import br.edu.loja.dto.CupomRequest;
import br.edu.loja.exception.*;
import br.edu.loja.repository.CupomDescontoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CupomDescontoService {
    private final CupomDescontoRepository repo;

    public CupomDescontoService(CupomDescontoRepository repo) { this.repo = repo; }

    @Transactional
    public CupomDesconto criar(CupomRequest r) {
        if (repo.existsByCodigoIgnoreCase(r.codigo())) throw new RegraNegocioException("Código de cupom já existe");
        if (r.tipo() == TipoDesconto.PERCENTUAL && r.valor().compareTo(new BigDecimal("100")) > 0) {
            throw new RegraNegocioException("Desconto percentual não pode ser maior que 100%");
        }
        return repo.save(new CupomDesconto(r.codigo(), r.tipo(), r.valor(), r.minimoPedido(), r.validade(), r.limiteUsos(), r.ativo()));
    }

    @Transactional(readOnly = true)
    public List<CupomDesconto> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public CupomDesconto buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cupom não encontrado"));
    }

    @Transactional
    public CupomDesconto atualizarControle(Long id, Boolean ativo, LocalDateTime validade, Integer limiteUsos) {
        CupomDesconto cupom = buscar(id);
        cupom.atualizar(ativo, validade, limiteUsos);
        return cupom;
    }

    @Transactional
    public BigDecimal aplicar(String codigo, BigDecimal subtotal) {
        CupomDesconto cupom = repo.findByCodigoIgnoreCase(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cupom não encontrado"));
        if (!cupom.podeUsar(subtotal, LocalDateTime.now())) throw new RegraNegocioException("Cupom inválido, expirado, esgotado ou abaixo do valor mínimo");
        BigDecimal desconto = cupom.calcularDesconto(subtotal);
        cupom.registrarUso();
        return desconto;
    }
}
