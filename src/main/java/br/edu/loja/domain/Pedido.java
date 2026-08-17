package br.edu.loja.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Cliente cliente;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal frete;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal desconto = BigDecimal.ZERO;
    @Column(length = 40)
    private String codigoCupom;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private StatusPedido status = StatusPedido.CRIADO;
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    protected Pedido() {}
    public Pedido(Cliente cliente, BigDecimal frete) { this.cliente = cliente; this.frete = frete; }

    public void adicionarItem(ItemPedido item) { itens.add(item); }
    public BigDecimal calcularSubtotal() { return itens.stream().map(ItemPedido::calcularSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add); }
    public BigDecimal calcularTotal() { return calcularSubtotal().add(frete).subtract(desconto).max(BigDecimal.ZERO); }

    public void aplicarDesconto(String codigoCupom, BigDecimal desconto) {
        if (desconto == null || desconto.signum() < 0) throw new IllegalArgumentException("Desconto inválido");
        this.codigoCupom = codigoCupom;
        this.desconto = desconto.min(calcularSubtotal());
    }

    public void alterarStatus(StatusPedido novoStatus) {
        if (status == StatusPedido.CANCELADO || status == StatusPedido.ENTREGUE) throw new IllegalStateException("Pedido finalizado não pode mudar de status");
        if (novoStatus == StatusPedido.CANCELADO) throw new IllegalStateException("Use o endpoint de cancelamento para cancelar um pedido");
        this.status = novoStatus;
    }

    public void cancelar() {
        if (status == StatusPedido.ENTREGUE) throw new IllegalStateException("Pedido entregue não pode ser cancelado");
        status = StatusPedido.CANCELADO;
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public BigDecimal getFrete() { return frete; }
    public BigDecimal getDesconto() { return desconto; }
    public String getCodigoCupom() { return codigoCupom; }
    public StatusPedido getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public BigDecimal getSubtotal() { return calcularSubtotal(); }
    public BigDecimal getTotal() { return calcularTotal(); }
}
