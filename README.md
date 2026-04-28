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

## Report - Theory Questions

### Part 1 - Q1: JAX-RS Resource Class Lifecycle
By default JAX-RS creates a new instance of a resource class for every incoming request. This is called per-request lifecycle. Because of this, storing shared data inside resource class fields is not safe since the data would be lost after each request ends. To handle this, a static ConcurrentHashMap was used in DataStore.java. Static fields belong to the class itself not the instance, so they survive across all requests. ConcurrentHashMap is thread safe which means multiple requests hitting the API at the same time will not corrupt each other's data or cause race conditions.

### Part 1 - Q2: HATEOAS
HATEOAS stands for Hypermedia as the Engine of Application State. It means including links inside API responses that guide the client to related resources. For example the Discovery endpoint returns links to rooms and sensors. This is considered advanced REST design because it makes the API self descriptive like a website where you follow links rather than memorizing URLs. Compared to static documentation, HATEOAS is better because the links are always current and clients can navigate dynamically without hardcoding URLs that might change.

### Part 2 - Q3: Returning IDs vs Full Objects
Returning only IDs uses less network bandwidth but forces the client to make extra requests to fetch each room's details which is known as the N+1 problem. Returning full room objects uses more bandwidth but gives the client everything it needs in one request reducing round trips. For a small dataset like campus rooms returning full objects is the better choice.

### Part 2 - Q4: Is DELETE Idempotent
Yes DELETE is idempotent. The first DELETE removes the room and returns 200. Any further DELETE on the same room returns 404. Even though the response code is different, the server state remains the same after each call which is that the room does not exist. Idempotency is about the effect on the server state not the response code.

### Part 3 - Q5: @Consumes Mismatch
If a client sends data as text/plain or application/xml instead of application/json, JAX-RS automatically returns a 415 Unsupported Media Type error. The request never reaches the resource method because JAX-RS rejects it at the framework level before the code runs.

### Part 3 - Q6: @QueryParam vs Path Param
Using @QueryParam like GET /sensors?type=CO2 is better for filtering because query parameters are optional by nature. Without them you get all sensors. With a path param like /sensors/type/CO2 the type becomes a mandatory part of the URL making it awkward to retrieve all sensors without filtering. Query parameters are the standard REST convention for filtering and searching collections.

### Part 4 - Q7: Sub-Resource Locator Benefits
The Sub-Resource Locator pattern improves code organisation by delegating responsibility to separate classes. Instead of one massive resource class handling every nested path, each class has a single responsibility. SensorResource handles sensor operations and SensorReadingResource handles readings logic. This makes the code easier to maintain, test and extend especially in large APIs with many nested resources.

### Part 5 - Q8: HTTP 422 vs 404
404 means the requested URL was not found. But in this case the URL /sensors is valid and the problem is that the roomId inside the JSON body references a room that does not exist. The request was understood but cannot be processed due to a semantic validation failure inside the payload. HTTP 422 Unprocessable Entity is more accurate because it tells the client that the JSON was valid but the content is logically incorrect.

### Part 5 - Q9: Stack Trace Security Risks
Exposing Java stack traces reveals sensitive internal information including package and class names, file paths on the server, library and framework versions which can be matched to known vulnerabilities, and method names with line numbers that help attackers understand the code logic. With this information an attacker can craft targeted attacks exploiting specific weaknesses in the identified libraries or framework versions.

### Part 5 - Q10: Filters vs Manual Logging
Manually adding Logger.info() to every resource method violates the DRY principle and is error prone since you might forget some methods. JAX-RS filters implement logging as a cross cutting concern where one filter class automatically applies to every single request and response without touching any resource class. If the logging format needs to change, it only needs to be updated in one place instead of dozens of methods.