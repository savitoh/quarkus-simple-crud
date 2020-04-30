package com.github.savitoh.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.testcontainers.containers.MySQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class DataSourceTestConfig implements QuarkusTestResourceLifecycleManager{

    private static final String MYSQL_VERSION = "mysql/mysql-server:8.0.19";
    private static MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(MYSQL_VERSION);
    
	@Override
	public Map<String, String> start() {
        MYSQL_CONTAINER.start();
        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.datasource.url", MYSQL_CONTAINER.getJdbcUrl());
        properties.put("quarkus.datasource.username", MYSQL_CONTAINER.getUsername());
        properties.put("quarkus.datasource.password", MYSQL_CONTAINER.getPassword());
		return properties;
	}

	@Override
	public void stop() {
        if(Objects.nonNull(MYSQL_CONTAINER)) {
            MYSQL_CONTAINER.stop();
        }
	}

}