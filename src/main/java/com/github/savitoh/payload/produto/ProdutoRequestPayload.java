package com.github.savitoh.payload.produto;

import java.math.BigDecimal;

public class ProdutoRequestPayload {

    private String nome;

    private BigDecimal valor;

    /**
     * @deprecated(usado apenas para fazer desserialização do JSON)
     */
    @Deprecated(since = "Desde Criação", forRemoval = false)
	public ProdutoRequestPayload() {
		//Default Constructor
	}

	public ProdutoRequestPayload(String nome, BigDecimal valor) {
		this.nome = nome;
		this.valor = valor;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
    }

	public String getNome() {
		return nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

}