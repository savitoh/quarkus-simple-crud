package com.github.savitoh.resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.github.savitoh.entity.Produto;
import com.github.savitoh.payload.produto.ProdutoRequestPayload;
import com.github.savitoh.payload.produto.ProdutoResponsePayload;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


@Path("api/v1/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @GET
    public List<ProdutoResponsePayload> buscarTodosProdutos() {
        final List<Produto> produtos = Produto.listAll();
        return produtos.stream()
                    .map(ProdutoResponsePayload::new)
                    .collect(Collectors.toList());
    }

    @POST
    @Transactional
    public Response criar(final ProdutoRequestPayload produtoPayload, @Context UriInfo uriInfo) {
        final var novoProduto = new Produto(produtoPayload);
        novoProduto.persistAndFlush();
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(novoProduto.id));
        return Response.created(uriBuilder.build()).build();
    }

    @PUT
    @Path("{codigo}")
    @Transactional
    public void atualizar(@PathParam("codigo") final Long codigo, final ProdutoRequestPayload produtoPayload) {
        final Optional<Produto> possivelProduto = Produto.findByIdOptional(codigo);
        possivelProduto.map(produto -> {
                    produto.nome = produtoPayload.getNome();
                    produto.valor = produtoPayload.getValor();
                    produto.persist();
                    return produto;
                })
                .orElseThrow(NotFoundException::new);
    }

    @DELETE
    @Path("{codigo}")
    @Transactional   
    public void deletar(@PathParam("codigo") final Long codigo) {
        Produto.findByIdOptional(codigo)
            .ifPresentOrElse(PanacheEntityBase::delete, () -> {
                throw new NotFoundException();
            });
    }

}