# Rental App

## Project Description

The Rental App is a Spring Boot application designed to manage property rentals. It allows users to browse available rental properties, and authenticated users to list new properties, and manage their existing listings. The application provides a RESTful API for these operations.

## Prerequisites

*   Java Development Kit (JDK) 17 or later
*   Maven 3.6.3 or later
*   MySQL Server 8.0 or later
*   Postman (for API testing)
*   Mockoon (for mocking API responses if needed)

## Setup and Launch Instructions

### 1. MySQL Database Setup

This application requires a MySQL database. Follow these steps to set it up:

1.  **Install MySQL Server:**
    If you don't have MySQL installed, download and install it from the [official MySQL website](https://dev.mysql.com/downloads/mysql/).

2.  **Create the Database:**
    Connect to your MySQL server using a client (e.g., MySQL Workbench, DBeaver, or the command line). Create a new database for the application. For example:
    ```sql
    CREATE DATABASE rental_app_db;
    ```

3.  **Configure Database Connection:**
    Open the `src/main/resources/application.properties` file.
    Update the following properties to match your MySQL setup:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/rental_app_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update # Or 'create' for the first run to auto-generate schema
    ```
    Replace `your_mysql_username` and `your_mysql_password` with your MySQL credentials. The `spring.jpa.hibernate.ddl-auto=update` property will allow Hibernate to automatically create or update the database schema based on your entity definitions. For the very first run, you might use `create` to ensure tables are generated.

### 2. Build and Run the Application

1.  **Clone the Repository (if you haven't already):**
    ```bash
    git clone <repository-url>
    cd rentalapp
    ```

2.  **Build the Project using Maven:**
    Navigate to the project's root directory (where `pom.xml` is located) and run:
    ```bash
    mvn clean install
    ```

3.  **Run the Application:**
    You can run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the packaged JAR file (after `mvn clean install`):
    ```bash
    java -jar target/rentalapp-0.0.1-SNAPSHOT.jar
    ```
    The application will typically start on port `3001` (as configured or by default if not specified otherwise in `application.properties` via `server.port`).

### 3. API Documentation (Swagger UI)

Once the application is running, you can access the Swagger UI for API documentation and testing in your browser:

*   **URL:** [http://localhost:3001/swagger-ui/index.html](http://localhost:3001/swagger-ui/index.html)

Swagger UI provides a user-friendly interface to explore the available API endpoints, view their request/response structures, and execute test calls directly from the browser.

### 4. Using the Postman Collection

A Postman collection is provided with the project in the "ressources" directory or as a `.json` file to help you test the API endpoints.

1.  **Import the Collection:**
    *   Open Postman.
    *   Click on "Import" (usually in the top-left corner).
    *   Drag and drop the Postman collection JSON file (e.g., `RentalApp.postman_collection.json`) or select it via the file browser.
    *   The collection will appear in your Postman workspace.

2.  **Using the Collection:**
    *   The collection contains pre-configured requests for various API endpoints.
    *   You might need to set up environment variables in Postman (e.g., for `baseUrl` like `http://localhost:3001/api`) if the collection uses them.
    *   Select a request, review its parameters, headers, and body, and click "Send" to test the endpoint.
    *   Pay attention to authentication requirements for certain endpoints (e.g., obtaining a JWT token from the login endpoint and using it in the `Authorization` header for protected routes). Only the login and registration endpoints are publicly accessible without authentication.
    *   For endpoints that require authentication, you can set the `Authorization` header in Postman with the JWT token obtained from the login endpoint by clicking on the "Bearer Token" option.
    * Regarding the create route for new rentals creation, make sure to select an actual (rather small) image file to upload, as the API expects a file in the request body. The field filled by default is a placeholder and will not work without a valid image file.
### 5. Using the Mockoon Environment

A Mockoon environment file is provided for the routes reference and the API behaviors.

1.  **Install Mockoon:**
    Download and install Mockoon from [mockoon.com](https://mockoon.com/).

2.  **Import the Environment:**
    *   Open Mockoon.
    *   Click on "Open environment" or the folder icon.
    *   Select the Mockoon environment JSON file.

3.  **Run the Mock Environment:**
    *   Once imported, the mock routes will be listed.

