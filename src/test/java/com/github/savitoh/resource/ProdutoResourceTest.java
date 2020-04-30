package com.github.savitoh.resource;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import javax.transaction.Transactional;

import com.github.savitoh.Helpers.TestUtils.HttpHeadersUtils;
import com.github.savitoh.config.DataSourceTestConfig;
import com.github.savitoh.entity.Produto;
import com.github.savitoh.payload.produto.ProdutoRequestPayload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@QuarkusTest
@QuarkusTestResource(DataSourceTestConfig.class)
public class ProdutoResourceTest {

    private static final String NOME_PADRAO = "Produto 1";
    private static final String NOME_ATUALIZADO = "Produto 1 (Atualizado)";
    private static final BigDecimal VALOR_PADRAO = BigDecimal.valueOf(200.00);
    private static final BigDecimal VALOR_ATUALIZADO = BigDecimal.valueOf(100.00);
    
    private static final String API_PRODUTOS_URI = "api/v1/produtos";
    private static final String PATH_PARAM_CODIGO_LIVRO = "codigo";

    @BeforeEach
    @Transactional
    public void excluiRegistrosTabelaProdutos() {
        Produto.deleteAll();
    }

    @Test
    public void deve_retornar_empty_list_quando_nao_possuir_produtos() {
        given()
        .when()
            .get(API_PRODUTOS_URI)
        .then()
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .statusCode(OK.getStatusCode())
            .body("", equalTo(Collections.EMPTY_LIST));
    }

    @Test
    public void nao_atualiza_quando_nao_existir_produto_com_codigo_informado() {
        var produtoRequestPayload = new ProdutoRequestPayload(NOME_ATUALIZADO, VALOR_ATUALIZADO);
        final Long codigoFail = 0L;
        
        given()
            .header(ACCEPT, APPLICATION_JSON)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .pathParam(PATH_PARAM_CODIGO_LIVRO, codigoFail)
            .body(produtoRequestPayload)
        .when()
            .put(API_PRODUTOS_URI.concat("/{codigo}"))
        .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void nao_deleta_quando_nao_existir_produto_com_codigo_informado() {
        final Long codigoFail = 0L;
        
        given()
            .pathParam(PATH_PARAM_CODIGO_LIVRO, codigoFail)
        .when()
            .delete(API_PRODUTOS_URI.concat("/{codigo}"))
        .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void deve_criar_produto() {
        var produtoRequestPayload = new ProdutoRequestPayload(NOME_PADRAO, VALOR_PADRAO);

        ExtractableResponse<Response> extractableResponse =  
            given()
                .header(ACCEPT, APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body(produtoRequestPayload)
            .when()
                .post(API_PRODUTOS_URI)
            .then()
                .statusCode(CREATED.getStatusCode())
            .extract();

        var headers = extractableResponse.headers();

        assertTrue(headers.hasHeaderWithName(LOCATION));
    }

    @Test
    public void deve_deletar_produto() {
        var createProdutoRequestPayload = new ProdutoRequestPayload(NOME_PADRAO, VALOR_PADRAO);

        Response response = 
            given()
                .header(ACCEPT, APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body(createProdutoRequestPayload)
            .when()
                .post(API_PRODUTOS_URI)
            .andReturn();

        final Optional<String> identificadorRecursoCriado = recuperaIdentificadorRecurso(response);

        given()
            .pathParam(PATH_PARAM_CODIGO_LIVRO, identificadorRecursoCriado.orElseThrow(RuntimeException::new))
        .when()
            .delete(API_PRODUTOS_URI.concat("/{codigo}"))
        .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }

    @Test
    public void deve_atualizar_produto() {
        var createProdutoRequestPayload = new ProdutoRequestPayload(NOME_PADRAO, VALOR_PADRAO);
        var updateProdutoRequestPayload = new ProdutoRequestPayload(NOME_ATUALIZADO, VALOR_ATUALIZADO);

        Response response = 
            given()
                .header(ACCEPT, APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .body(createProdutoRequestPayload)
            .when()
                .post(API_PRODUTOS_URI)
            .andReturn();

        final Optional<String> identificadorRecursoCriado = recuperaIdentificadorRecurso(response);
            
        given()
            .header(ACCEPT, APPLICATION_JSON)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .pathParam(PATH_PARAM_CODIGO_LIVRO, identificadorRecursoCriado.orElseThrow(RuntimeException::new))
            .body(updateProdutoRequestPayload)
        .when()
            .put(API_PRODUTOS_URI.concat("/{codigo}"))
        .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }

    private Optional<String> recuperaIdentificadorRecurso(Response response) {
        var headers = response.headers();
        return HttpHeadersUtils.recuperaIdentificadorRecursoAPartirHeaderLocation(headers);
    }

}