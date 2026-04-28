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
##REPORT
Name: Sai Pradeep               IIT ID: 20241324      UOW ID: w21213551
Q1
Question:In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.
So as per the lifecycle of a JAX-RS resource class it is a Per-request so every time someone calls a API , the JAX-RS creates a brand new instance of the resource class, therefore every request will have its own new object this means data would be lost after the request finishes therefore in order to store the data from a request I have used a static ConcurrentHashMap , as we know static keyword means it belongs to the class and not to the object and ConcurrentHashMap is thread-safe there for it can handle multiple request that the same time without causing any problem.

Q2
Question: Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?
So HATEOAS stands for “Hypermedia as the Engine of Application State” , this means that the API responses contain links to the related resources of the response , it is considered as advanced because most APIs return data where as HATEOAS makes the API self descriptive – it tells the client what it can do next.Also the benefit of using it is that the client does not need to memorize the URLs and even if the URLs change it does not break the client.
Q3
Question: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.
In terms of bandwidth returning only IDs uses less network bandwidth compared to returning full objects , but then in terms of client side processing when returning full room objects everything is given in one request rather than individual request to fetch each and everything .
Q4 
Question: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.
So the DELETE operation is idempotent because first time the DELETE operation removes the room and returns 200 , so again when the user intends to Delete the same room it will return 404 Not Found as the room does not exists , so the result is always same because second time also it tries to delete the room rather than performing something else.
Q5
We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch.
If a client sends data as text/plain or application/xml instead of application/json, the JAX-RS will automatically return 415 Unsupported Media Type and JAX-RS rejects it at the framework level before the code runs 
Q6
Question:You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?
So using @QueryParam like GET /sensors?type=CO2 is better for filtering because if u did not inclusion of the parameters are optional and without the parameters we would resulted by all the sensors , where as with path param it make the type a mandatory part of the url and making thing complicated. Query parameters are the standard REST convention for filtering, searching, and sorting collections.
Q7
Question: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?
The Sub-Resource Locator pattern improves code organisation by delegating responsibility to separate classes. Instead of one massive resource class handling every nested path, each class has a single responsibility. SensorResource handles sensor operations and SensorReadingResource handles readings. This makes the code easier to maintain, test, and extend. In large APIs with many nested resources, this pattern prevents resource classes from becoming unmanageable.
Q8
Question: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload
404 means the requested URL/resource was not found and 422 occurs when the server understands the request and the syntax is correct, but the data provided is semantically wrong therefore here also the URL “/sensors” is perfectly valid but the problem is in the roomID value inside the JSON body reference a room that doesn’t exist, so here also the request is valid but content is logically incorrect.
Q9
Question: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace
Exposing the Java stack traces makes the sensitive information vulnerable to the hackers , the sensitive information include : package and class names revealing the code structure, file paths on the server, library and framework versions which can be matched to known vulnerabilities, method names and line numbers, the exposure of these help the hackers to understand the logical flow.





