package com.github.savitoh.Helpers.TestUtils;

import static javax.ws.rs.core.HttpHeaders.LOCATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.Test;

import io.restassured.http.Header;
import io.restassured.http.Headers;

public class HttpHeadersUtilsTest {

    @Test
    public void testRecuperaIdentificadorRecursoAPartirHeaderLocationComParametroNulloDeveLancarException() {
        Headers headers = null;

        assertThrows(IllegalArgumentException.class, 
                    () -> HttpHeadersUtils.recuperaIdentificadorRecursoAPartirHeaderLocation(headers),
                    "Param #headers não pode ser nulo");  
    }

    @Test
    public void testRecuperaIdentificadorRecursoAPartirHeaderLocationHeadersSemLocation() {
        Headers headers = new Headers();

        Optional<String> result = HttpHeadersUtils.recuperaIdentificadorRecursoAPartirHeaderLocation(headers);  

        assertTrue(result.isEmpty());
    }

    @Test
    public void testRecuperaIdentificadorRecursoAPartirHeaderLocation() {
        final String locationValue = "http://localhost:8081/api/v1/fakeresource/1";
        Header headerLocation = new Header(LOCATION, locationValue);
        Headers headers = new Headers(headerLocation);
        
        Optional<String> result = HttpHeadersUtils.recuperaIdentificadorRecursoAPartirHeaderLocation(headers);

        assertTrue(result.isPresent());
        assertEquals("1", result.get());
    }
}