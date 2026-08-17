package br.edu.loja.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrinhos")
public class Carrinho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false)
    private Cliente cliente;
    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrinho> itens = new ArrayList<>();

    protected Carrinho() {}
    public Carrinho(Cliente cliente) { this.cliente = cliente; }

    public BigDecimal calcularSubtotal() {
        return itens.stream().map(ItemCarrinho::calcularSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<ItemCarrinho> getItens() { return itens; }
}
