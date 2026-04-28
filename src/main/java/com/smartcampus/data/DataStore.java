package com.smartcampus.data;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    // All rooms stored by their ID
    public static final Map<String, Room> rooms 
            = new ConcurrentHashMap<>();

    // All sensors stored by their ID
    public static final Map<String, Sensor> sensors 
            = new ConcurrentHashMap<>();

    // All readings stored by sensor ID
    // Each sensor has a LIST of readings
    public static final Map<String, List<SensorReading>> sensorReadings 
            = new ConcurrentHashMap<>();

    // Pre-load some sample data when app starts
    static {
        // Create 3 sample rooms
        Room room1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room room2 = new Room("CS-101", "Computer Lab 1", 30);
        Room room3 = new Room("HALL-01", "Main Hall", 200);

        rooms.put(room1.getId(), room1);
        rooms.put(room2.getId(), room2);
        rooms.put(room3.getId(), room3);

        // Create 3 sample sensors
        Sensor sensor1 = new Sensor(
                "TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor sensor2 = new Sensor(
                "CO2-001", "CO2", "ACTIVE", 400.0, "CS-101");
        Sensor sensor3 = new Sensor(
                "OCC-001", "Occupancy", "MAINTENANCE", 0.0, "HALL-01");

        sensors.put(sensor1.getId(), sensor1);
        sensors.put(sensor2.getId(), sensor2);
        sensors.put(sensor3.getId(), sensor3);

        // Link sensors to their rooms
        room1.getSensorIds().add(sensor1.getId());
        room2.getSensorIds().add(sensor2.getId());
        room3.getSensorIds().add(sensor3.getId());

        // Create empty reading lists for each sensor
        sensorReadings.put(sensor1.getId(), new ArrayList<>());
        sensorReadings.put(sensor2.getId(), new ArrayList<>());
        sensorReadings.put(sensor3.getId(), new ArrayList<>());
    }
}