
package com.smartcampus.exceptions;

import jakarta.ws.rs.*;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyExceptions> {
    @Override
    public Response toResponse(RoomNotEmptyExceptions e){
        String message = "{\"error\": \"" + e.getMessage() + "\"}";
        return Response.status(409).entity(message).type(MediaType.APPLICATION_JSON).build();
    }
}
