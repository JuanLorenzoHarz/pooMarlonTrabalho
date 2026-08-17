package br.edu.loja.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupons_desconto", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
public class CupomDesconto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDesconto tipo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal minimoPedido = BigDecimal.ZERO;

    private LocalDateTime validade;

    private Integer limiteUsos;

    @Column(nullable = false)
    private Integer usos = 0;

    @Column(nullable = false)
    private Boolean ativo = true;

    protected CupomDesconto() {}

    public CupomDesconto(String codigo, TipoDesconto tipo, BigDecimal valor, BigDecimal minimoPedido,
                         LocalDateTime validade, Integer limiteUsos, Boolean ativo) {
        this.codigo = codigo.toUpperCase();
        this.tipo = tipo;
        this.valor = valor;
        this.minimoPedido = minimoPedido == null ? BigDecimal.ZERO : minimoPedido;
        this.validade = validade;
        this.limiteUsos = limiteUsos;
        this.ativo = ativo == null || ativo;
    }

    public boolean podeUsar(BigDecimal subtotal, LocalDateTime agora) {
        if (!ativo || subtotal.compareTo(minimoPedido) < 0) return false;
        if (validade != null && agora.isAfter(validade)) return false;
        return limiteUsos == null || usos < limiteUsos;
    }

    public BigDecimal calcularDesconto(BigDecimal subtotal) {
        BigDecimal desconto = tipo == TipoDesconto.PERCENTUAL
                ? subtotal.multiply(valor).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                : valor;
        return desconto.min(subtotal).setScale(2, RoundingMode.HALF_UP);
    }

    public void registrarUso() { usos++; }

    public void atualizar(Boolean ativo, LocalDateTime validade, Integer limiteUsos) {
        if (ativo != null) this.ativo = ativo;
        this.validade = validade;
        this.limiteUsos = limiteUsos;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public TipoDesconto getTipo() { return tipo; }
    public BigDecimal getValor() { return valor; }
    public BigDecimal getMinimoPedido() { return minimoPedido; }
    public LocalDateTime getValidade() { return validade; }
    public Integer getLimiteUsos() { return limiteUsos; }
    public Integer getUsos() { return usos; }
    public Boolean getAtivo() { return ativo; }
}
