package br.edu.loja.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_carrinho")
public class ItemCarrinho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(optional = false)
    private Carrinho carrinho;
    @ManyToOne(optional = false)
    private Produto produto;
    @Column(nullable = false)
    private Integer quantidade;

    protected ItemCarrinho() {}
    public ItemCarrinho(Carrinho carrinho, Produto produto, Integer quantidade) {
        this.carrinho = carrinho;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public void alterarQuantidade(int quantidade) { this.quantidade = quantidade; }
    public BigDecimal calcularSubtotal() { return produto.getPreco().multiply(BigDecimal.valueOf(quantidade)); }

    public Long getId() { return id; }
    public Produto getProduto() { return produto; }
    public Integer getQuantidade() { return quantidade; }
}
