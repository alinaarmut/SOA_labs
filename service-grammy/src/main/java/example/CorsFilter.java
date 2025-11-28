package example;

import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

        @Override
        public void filter(jakarta.ws.rs.container.ContainerRequestContext requestContext,
                           ContainerResponseContext responseContext) throws IOException {

            String origin = requestContext.getHeaderString("Origin");

            if ("https://se.ifmo.ru".equals(origin) ||
                    "https://helios.cs.ifmo.ru".equals(origin)) {
                responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
                responseContext.getHeaders().putSingle("Vary", "Origin");
            }

            responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Headers",
                    "Origin, Content-Type, Accept, Authorization");
            responseContext.getHeaders().putSingle("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            responseContext.getHeaders().putSingle("Access-Control-Max-Age", "1209600");
        }
    }

