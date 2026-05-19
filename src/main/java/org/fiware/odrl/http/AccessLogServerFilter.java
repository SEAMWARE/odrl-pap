package org.fiware.odrl.http;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Provider
@ApplicationScoped
public class AccessLogServerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String START_TIME = "access-log.start-time";

    @Inject
    AccessLogServerConfiguration config;

    @Inject
    RoutingContext routingContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (config.excludePaths().map(paths -> paths.stream().anyMatch(requestContext.getUriInfo().getPath()::startsWith)).orElse(false)) {
            return;
        }
        requestContext.setProperty(START_TIME, System.currentTimeMillis());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        Long start = (Long) requestContext.getProperty(START_TIME);
        if (start == null) return;

        long duration = System.currentTimeMillis() - start;
        String method = requestContext.getMethod();
        URI requestUri = requestContext.getUriInfo().getRequestUri();
        String query = requestUri.getQuery();
        String uri = query != null ? requestUri.getPath() + "?" + query : requestUri.getPath();
        int status = responseContext.getStatus();
        String remoteIp = routingContext.request().remoteAddress().host();
        String protocol = routingContext.request().version().alpnName().toUpperCase();
        String forwardedFor = requestContext.getHeaderString("X-Forwarded-For");

        if (forwardedFor != null) {
            log.info("{} [{}] - {} - {} {} {} - {}ms", forwardedFor, remoteIp, protocol, method, uri, status, duration);
        } else {
            log.info("{} - {} - {} {} {} - {}ms", remoteIp, protocol, method, uri, status, duration);
        }
    }

    @ConfigMapping(prefix = "http.server.log")
    public interface AccessLogServerConfiguration {
        @WithName("exclude-paths")
        Optional<List<String>> excludePaths();
    }
}
