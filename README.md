# iFutureTaskTwo

`iFutureTaskTwo` is a Java web application that displays and manages a tree-like file structure. The project is designed to be deployed to Apache Tomcat and persists the tree state in PostgreSQL through Hibernate.

## Features

- Loads the tree hierarchy from PostgreSQL on startup
- Creates a sample tree automatically when the database is empty
- Supports adding, deleting, copying, and moving tree nodes
- Keeps tree operations synchronized with the database
- Simulates a slow folder expansion with an artificial 2-second delay

## Tech Stack

- Java 8
- Spring MVC
- Hibernate
- PostgreSQL
- Maven
- Apache Tomcat

## Project Structure

- `src/main/java` - application source code
- `src/main/resources/hibernate.cfg.xml` - Hibernate and database connection settings
- `web` - JSP and web application resources
- `dbBackup` - database backup shipped with the repository

## Build

Use Maven to compile and package the application:

```bash
mvn clean package
```

The build produces a WAR archive suitable for deployment to Tomcat.

## Run Locally

1. Create a PostgreSQL database.
2. Update the connection settings in `src/main/resources/hibernate.cfg.xml`.
3. Build the project with Maven.
4. Deploy the generated WAR file to Tomcat.

If the application starts against an empty database, it creates a test tree automatically.

## Testing

There are currently no automated tests in the repository. The project can still be validated with:

```bash
mvn test
```

At the moment, this command completes successfully but reports that there are no tests to run.
