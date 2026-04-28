package com.smartcampus.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    
    @Override
    public Response toResponse(Throwable e) {
        String message = "{\"error\": \"Internal Server Error. Please try again later.\"}";
        return Response.status(500)
                       .entity(message)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
