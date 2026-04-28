package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.models.Sensor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Produces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SensorResource{
    
    @POST
    public Response createSensor(Sensor sensor){
        
       if(!(DataStore.rooms.containsKey(sensor.getRoomId()))){
           throw new LinkedResourceNotFoundException(sensor.getRoomId());
       }
       DataStore.sensors.put(sensor.getId(),sensor);
       return Response.ok(sensor).build();
    }
    
    @GET
    
    public Response getAllSensors(@QueryParam("type") String type){
      
        List<Sensor> sensorList = new ArrayList<>(DataStore.sensors.values());
        if(type != null && !type.isBlank()){
           List<Sensor> filtered = new ArrayList<>();
           for(Sensor s : sensorList){
              if(s.getType().equalsIgnoreCase(type)){
                filtered.add(s);
              }
           }
           return Response.ok(filtered).build();
        }
      return Response.ok(sensorList).build();
    }
    
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId){
        if(!(DataStore.sensors.containsKey(sensorId))){
            String message = "{\"error\": \"Sensor not found with ID: " + sensorId + "\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }
        Sensor s = DataStore.sensors.get(sensorId);
        return Response.ok(s).build();
    }
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
    return new SensorReadingResource(sensorId);
    }
    
    
    









}