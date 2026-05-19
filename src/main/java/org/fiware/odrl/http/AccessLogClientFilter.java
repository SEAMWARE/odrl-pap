package org.fiware.odrl.http;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class AccessLogClientFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final String START_TIME = "access-log.start-time";

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext.setProperty(START_TIME, System.currentTimeMillis());
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        long duration = System.currentTimeMillis() - (long) requestContext.getProperty(START_TIME);
        int status = responseContext.getStatus();

        if (status >= 400) {
            log.warn("{} {} {} - {}ms", requestContext.getMethod(), requestContext.getUri(), status, duration);
        } else {
            log.debug("{} {} {} - {}ms", requestContext.getMethod(), requestContext.getUri(), status, duration);
        }
    }
}
