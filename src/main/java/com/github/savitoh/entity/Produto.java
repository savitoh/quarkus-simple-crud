package com.github.savitoh.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;

import com.github.savitoh.payload.produto.ProdutoRequestPayload;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Produto extends PanacheEntity {

    public String nome;

    public BigDecimal valor;

    @CreationTimestamp
    public LocalDateTime criacao;

    @UpdateTimestamp
    public LocalDateTime atualizacao;

    /**
     * Usado apenas pelo Hibernate
     */
    @Deprecated(since = "always", forRemoval = false)
    public Produto() {

    }

	public Produto(ProdutoRequestPayload produtoPayload) {
        this.nome = produtoPayload.getNome();
        this.valor = produtoPayload.getValor();
	}
}