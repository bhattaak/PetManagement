# Pet Management System

This project is a simple Pet Management System developed using Spring Boot and Hibernate.

## Getting Started

To run this project locally, you need to have Java and Maven installed on your machine.

1. Clone the repository:
2. Navigate to the project directory:  cd tractive
3. Run the application using Maven: mvn spring-boot:run
4. The application will start on `localhost:8080`.

## Executing Tests:
1. mvn verify

## API Endpoints

### 1. Save a Pet

Endpoint: `/pets/save`

- Method: POST
- Description: Save a new pet with optional associated cat details.
  - Curl Command: curl -X POST http://localhost:8080/pets/save \
    -H 'Content-Type: application/json' \
    -d '{
    "pet": {
    "petType": "CAT",
    "trackerType": "SMALL",
    "ownerId": 12,
    "inZone": false
    },"cat": {
    "lostTracker": false
    }
    }'

### 2. Get All Pets

   Endpoint: /pets/all

- Method: GET
- Description: Retrieve all pets.
- Curl Command: curl http://localhost:8080/pets/all

### 3. Get Pets Outside the Zone Grouped by Type and Tracker

   Endpoint: /pets/outside-zone

- Method: GET
- Description: Retrieve pets outside the zone grouped by type and tracker.
- Curl Command: curl http://localhost:8080/pets/outside-zone






