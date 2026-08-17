package br.edu.loja.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    private Pedido pedido;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private FormaPagamento forma;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private StatusPagamento status = StatusPagamento.PENDENTE;
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    protected Pagamento() {}
    public Pagamento(Pedido pedido, BigDecimal valor, FormaPagamento forma) {
        this.pedido = pedido;
        this.valor = valor;
        this.forma = forma;
    }

    public void aprovar() {
        if (status != StatusPagamento.PENDENTE) throw new IllegalStateException("Somente pagamento pendente pode ser aprovado");
        status = StatusPagamento.APROVADO;
    }
    public void recusar() {
        if (status != StatusPagamento.PENDENTE) throw new IllegalStateException("Somente pagamento pendente pode ser recusado");
        status = StatusPagamento.RECUSADO;
    }
    public void reembolsar() {
        if (status != StatusPagamento.APROVADO) throw new IllegalStateException("Apenas pagamentos aprovados podem ser reembolsados");
        status = StatusPagamento.REEMBOLSADO;
    }

    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public BigDecimal getValor() { return valor; }
    public FormaPagamento getForma() { return forma; }
    public StatusPagamento getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
