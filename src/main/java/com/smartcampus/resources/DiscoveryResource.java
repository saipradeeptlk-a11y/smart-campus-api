package com.smartcampus.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

      @GET
      public Response discover(){
          
          //API metadata information
          Map<String,Object> response = new HashMap<>();
          
          // Version info
          response.put("name","Smart Campus API");
          response.put("version","1.0");
          response.put("description","Smart Campus Sensor and Room Management API");
          
          //Links to primary resource collection (HATEOAS)
          Map<String,String> links = new HashMap<>();
          links.put("rooms","http://localhost:8080/api/v1/rooms");
          links.put("sensors", "http://localhost:8080/api/v1/sensors");
          response.put("resources", links);
          
          //Admin contact details 
          Map<String, String> contact = new HashMap<>();
          contact.put("name", "Smart Campus Admin");
          contact.put("email", "admin@smartcampus.com");
          response.put("contact", contact);
          
          return Response.ok(response).build();
      
      
      
      
      
      
      
      
      
      
      
      }








}

