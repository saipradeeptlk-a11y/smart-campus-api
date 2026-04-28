package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.exceptions.RoomNotEmptyExceptions;
import com.smartcampus.models.Room;
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

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource{
    
    
    @GET 
    public Response getAllRooms(){
      List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
      return Response.ok(roomList).build();
    }
    
    @POST
    public Response createRoom(Room room){
      if(room.getId() == null || room.getId().isBlank()){
       String message = "{\"error\": \"Room ID is required\"}";
       return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
      }
      if(DataStore.rooms.containsKey(room.getId())){
       String message = "{\"error\" :\"Room with the ID: "+room.getId()+" Already exists\"}";
       return Response.status(Response.Status.CONFLICT).entity(message).build();
      }
      if(room.getSensorIds() == null ){
          room.setSensorIds(new ArrayList<>());
          
      }
      DataStore.rooms.put(room.getId(),room);
      return Response.ok(room).build();
      
      
    
    }
 
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId")String roomId){
         if(!(DataStore.rooms.containsKey(roomId))){
             String message ="{\"error\": \"Room not found with ID: " + roomId + "\"}";
             return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
         }
         Room r = DataStore.rooms.get(roomId);
         return Response.ok(r).build();
    }
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId")String roomId){
        
        if(!(DataStore.rooms.containsKey(roomId))){
             String message ="{\"error\": \"Room not found with ID: " + roomId + "\"}";
             return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
         }
         Room r = DataStore.rooms.get(roomId);
       
         
         if (r.getSensorIds() != null && !r.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyExceptions(roomId);
        }

        DataStore.rooms.remove(roomId);
        return Response.ok(r).build();
    
    
    }
}

