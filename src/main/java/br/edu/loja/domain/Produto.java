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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    protected Produto() {}

    public Produto(String nome, String descricao, BigDecimal preco, Integer estoque) {
        this(nome, descricao, preco, estoque, null);
    }

    public Produto(String nome, String descricao, BigDecimal preco, Integer estoque, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
    }

    public void atualizar(String nome, String descricao, BigDecimal preco, Integer estoque, Boolean ativo) {
        atualizar(nome, descricao, preco, estoque, ativo, this.categoria);
    }

    public void atualizar(String nome, String descricao, BigDecimal preco, Integer estoque, Boolean ativo, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = ativo;
        this.categoria = categoria;
    }

    public void reduzirEstoque(int quantidade) {
        if (quantidade <= 0 || estoque < quantidade) throw new IllegalArgumentException("Estoque insuficiente");
        estoque -= quantidade;
    }

    public void reporEstoque(int quantidade) { if (quantidade > 0) estoque += quantidade; }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
    public Boolean getAtivo() { return ativo; }
    public Categoria getCategoria() { return categoria; }
}
