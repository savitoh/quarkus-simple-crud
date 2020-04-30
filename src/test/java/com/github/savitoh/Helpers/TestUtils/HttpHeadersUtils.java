package com.github.savitoh.Helpers.TestUtils;

import static javax.ws.rs.core.HttpHeaders.LOCATION;

import java.util.Optional;

import org.wildfly.common.Assert;

import io.restassured.http.Headers;

public final class HttpHeadersUtils {

    private HttpHeadersUtils() {
        throw new UnsupportedOperationException();
    }

    public static Optional<String> recuperaIdentificadorRecursoAPartirHeaderLocation(Headers headers) {
        Assert.checkNotNullParam("Param #headers não pode ser nulo", headers);
        if(headers.hasHeaderWithName(LOCATION)) {
            final String locationValue = headers.getValue(LOCATION);
            String [] locationSplit = locationValue.split("/");
            final String lastPartLocationSplit = locationSplit[locationSplit.length - 1];
            return Optional.of(lastPartLocationSplit);
        }
        return Optional.empty();
    }

}