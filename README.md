# PapelariaABC-Website
A simple website for the "Papelaria ABC" store.

## Project Structure
- **src/main/java/controllers**: Contains Java classes that handle incoming HTTP requests and define the application's endpoints.
- **src/main/java/services**: Contains service classes that encapsulate the business logic of the application.
- **src/main/java/websocket**: Contains classes that manage WebSocket connections and handle real-time communication.
- **src/main/java/Application.java**: The main entry point of the Java application, initializing the Spring Boot application.
- **src/main/resources/static**: For static resources such as CSS, JavaScript, and images served directly to the client.
  - **css**: Contains CSS files for styling the frontend.
  - **js**: Contains JavaScript files for client-side functionality.
  - **images**: Contains image files used in the website.
- **src/main/resources/templates**: Contains HTML templates, such as index.html, which will be rendered by the backend.
- **src/main/resources/application.properties**: Contains configuration properties for the application, such as database connection settings and server port.
- **src/test/java**: Contains test classes for the Java application, ensuring that the application behaves as expected.
- **pom.xml**: The Maven configuration file that manages project dependencies and build settings.

## Setup Instructions
1. Clone the repository.
2. Navigate to the project directory.
3. Build the project using Maven: `mvn clean install`.
4. Run the application: `mvn spring-boot:run`.
5. Access the website at `http://localhost:8080`.