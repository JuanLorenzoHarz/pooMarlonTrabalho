package br.edu.loja.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    private Pedido pedido;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorSemJuros;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorJuros;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorParcela;
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal jurosPercentualMensal;
    @Column(nullable = false)
    private Integer parcelas;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private FormaPagamento forma;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private StatusPagamento status = StatusPagamento.PENDENTE;
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    protected Pagamento() {}

    public Pagamento(Pedido pedido, BigDecimal valorSemJuros, BigDecimal valorFinal, FormaPagamento forma,
                     Integer parcelas, BigDecimal jurosPercentualMensal) {
        this.pedido = pedido;
        this.valorSemJuros = valorSemJuros.setScale(2, RoundingMode.HALF_UP);
        this.valor = valorFinal.setScale(2, RoundingMode.HALF_UP);
        this.valorJuros = this.valor.subtract(this.valorSemJuros).setScale(2, RoundingMode.HALF_UP);
        this.forma = forma;
        this.parcelas = parcelas;
        this.jurosPercentualMensal = jurosPercentualMensal;
        this.valorParcela = this.valor.divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.HALF_UP);
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
    public BigDecimal getValorSemJuros() { return valorSemJuros; }
    public BigDecimal getValorJuros() { return valorJuros; }
    public BigDecimal getValor() { return valor; }
    public BigDecimal getValorParcela() { return valorParcela; }
    public BigDecimal getJurosPercentualMensal() { return jurosPercentualMensal; }
    public Integer getParcelas() { return parcelas; }
    public FormaPagamento getForma() { return forma; }
    public StatusPagamento getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
