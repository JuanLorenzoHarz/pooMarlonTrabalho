package br.edu.loja.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 300)
    private String descricao;

    @Column(nullable = false)
    private Boolean ativa = true;

    protected Categoria() {}

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public void atualizar(String nome, String descricao, Boolean ativa) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativa = ativa;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Boolean getAtiva() { return ativa; }
}
