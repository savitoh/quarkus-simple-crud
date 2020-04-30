package com.github.savitoh.entity;

import com.github.savitoh.config.DataSourceTestConfig;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(DataSourceTestConfig.class)
public class ProdutoTest {

    @Test
    public void testQuantidade() {
        Assert.assertEquals(0L, Produto.count());
    }

}