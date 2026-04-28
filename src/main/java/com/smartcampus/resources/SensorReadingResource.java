package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.exceptions.SensorUnavailableException;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SensorReadingResource {
    
  private final String sensorId;
 
  public SensorReadingResource(String sensorId){
     this.sensorId=sensorId;
  }  
  @GET
  public Response getReadings(){
      if(!(DataStore.sensors.containsKey(sensorId))) {
            String message = "{\"error\": \"Sensor not found with ID: " + sensorId + "\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }
       List<SensorReading> readings = DataStore.sensorReadings.getOrDefault(sensorId, new ArrayList<>());
       return Response.ok(readings).build();
  
  }
  @POST 
  public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if(sensor == null) {
            String message = "{\"error\": \"Sensor not found with ID: " + sensorId + "\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(message).build();
        }

        // Block if sensor is under MAINTENANCE
        if("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }

        // Auto generate ID and timestamp if not provided
        if(reading.getId() == null || reading.getId().isBlank()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if(reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        // Save the reading
        DataStore.sensorReadings
                .computeIfAbsent(sensorId, k -> new ArrayList<>())
                .add(reading);

        // Update parent sensor's currentValue
        sensor.setCurrentValue(reading.getValue());

        String message = "{\"message\": \"Reading added successfully\"}";
        return Response.status(Response.Status.CREATED).entity(message).build();
    }

    
    
    
}
