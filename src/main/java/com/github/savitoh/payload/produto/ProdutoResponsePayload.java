package com.github.savitoh.payload.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;

import com.github.savitoh.entity.Produto;

public class ProdutoResponsePayload {

    private Long codigo;

    private String nome;

    private BigDecimal valor;

    @JsonbDateFormat(value = "dd-MM-YYYY HH:mm:ss")
    private LocalDateTime criacao;

    /**
     * Utilizado apenas para fazer Serialização do JSON
     */
    @Deprecated(since = "Desde Criação", forRemoval = false)
	public ProdutoResponsePayload() {
	}

    public ProdutoResponsePayload(final Produto produto) {
        this.codigo = produto.id;
        this.nome = produto.nome;
        this.valor = produto.valor;
        this.criacao = produto.criacao;
    }

    public Long getCodigo() {
        return codigo;
    }

	public String getNome() {
		return nome;
	}

	public BigDecimal getValor() {
		return valor;
    }
    
    public LocalDateTime getCriacao() {
        return criacao;
    }
}