package br.edu.loja.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(length = 500)
    private String descricao;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;
    @Column(nullable = false)
    private Integer estoque;
    @Column(nullable = false)
    private Boolean ativo = true;

    protected Produto() {}

    public Produto(String nome, String descricao, BigDecimal preco, Integer estoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public void atualizar(String nome, String descricao, BigDecimal preco, Integer estoque, Boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = ativo;
    }

    public void reduzirEstoque(int quantidade) {
        if (quantidade <= 0 || estoque < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }
        estoque -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        if (quantidade > 0) estoque += quantidade;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
    public Boolean getAtivo() { return ativo; }
}
