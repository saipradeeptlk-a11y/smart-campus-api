
package com.smartcampus.exceptions;
import jakarta.ws.rs.*;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException>{
    @Override
    public Response toResponse(LinkedResourceNotFoundException e){
        String message = "{\"error\": \"" + e.getMessage() + "\"}";
        return Response.status(422).entity(message).type(MediaType.APPLICATION_JSON).build();
    }
}
