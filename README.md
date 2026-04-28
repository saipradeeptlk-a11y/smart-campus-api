# Smart Campus API

A RESTful API built using JAX-RS (Jersey) for managing Rooms and Sensors across a university Smart Campus infrastructure.

---

## Technology Stack
- Java 11
- JAX-RS (Jakarta RESTful Web Services)
- Jersey 3.1.3
- Grizzly HTTP Server
- Jackson (JSON)
- Maven

---

## Project Structure

src/main/java/com/smartcampus/
├── Main.java
├── SmartCampusApplication.java
├── data/
│   └── DataStore.java
├── models/
│   ├── Room.java
│   ├── Sensor.java
│   └── SensorReading.java
├── resources/
│   ├── DiscoveryResource.java
│   ├── RoomResource.java
│   ├── SensorResource.java
│   ├── SensorReadingResource.java
│   └── LoggingFilter.java
└── exceptions/
├── RoomNotEmptyExceptions.java
├── RoomNotEmptyExceptionMapper.java
├── LinkedResourceNotFoundException.java
├── LinkedResourceNotFoundExceptionMapper.java
├── SensorUnavailableException.java
├── SensorUnavailableExceptionMapper.java
└── GlobalExceptionMapper.java

---

## How to Build and Run

### Prerequisites
- Java 11 or higher
- Maven 3.x

### Steps

**Step 1: Clone the repository**
```bash
git clone https://github.com/saipradeeptlk-a11y/smart-campus-api.git
cd smart-campus-api
```

**Step 2: Build the project**
```bash
mvn clean package
```

**Step 3: Run the server**
```bash
java -jar target/smart-campus-api-1.0-SNAPSHOT-jar-with-dependencies.jar
```

**Step 4: API is now running at** http://localhost:8080/api/v1                                    ---

## Sample curl Commands

**1. Get API Discovery**
```bash
curl http://localhost:8080/api/v1
```

**2. Get All Rooms**
```bash
curl http://localhost:8080/api/v1/rooms
```

**3. Create a New Room**
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"ENG-201","name":"Engineering Lab","capacity":40}'
```

**4. Get All Sensors filtered by type**
```bash
curl http://localhost:8080/api/v1/sensors?type=CO2
```

**5. Add a Reading to a Sensor**
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":23.5}'
```

---

## API Endpoints

### Discovery
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1 | API metadata and links |

### Rooms
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a new room |
| GET | /api/v1/rooms/{roomId} | Get a specific room |
| DELETE | /api/v1/rooms/{roomId} | Delete a room |

### Sensors
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/sensors | Get all sensors (optional ?type= filter) |
| POST | /api/v1/sensors | Create a new sensor |
| GET | /api/v1/sensors/{sensorId} | Get a specific sensor |

### Sensor Readings
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/sensors/{sensorId}/readings | Get all readings for a sensor |
| POST | /api/v1/sensors/{sensorId}/readings | Add a new reading |

---

## Error Handling

| HTTP Code | Scenario |
|-----------|----------|
| 400 | Bad request - missing required fields |
| 403 | Sensor is under MAINTENANCE |
| 404 | Resource not found |
| 409 | Room cannot be deleted - has active sensors |
| 422 | Sensor references a roomId that does not exist |
| 500 | Unexpected internal server error |

---

