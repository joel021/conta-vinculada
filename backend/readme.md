# contas-vinculadas-back

# 🛠️ Development Guidelines

This section outlines the recommended approach for maintaining and extending this project. Please follow these practices to ensure consistency and efficiency in development and testing.

#### 📁 Project Structure & Testing

* **Test Directory**: All tests are located under
  `backend/src/test/java/br/jus/trf1/sjba/contavinculada`
  The test package structure mirrors the main source package being tested.

* **Testing Levels**:

    * **Unit Tests**: Validate individual methods or small groups of methods.
    * **Integration Tests**: Verify the interaction between different modules or components.
    * **System Tests**: Execute and validate the entire system (automated, but following the same principles as manual tests, such as using Postman).
    * **End-to-End (E2E) Tests**: Cover the full application flow through the front-end.

#### 🧱 Project Configuration Recommendations

* **Base Structure**: Follow the same project structure and configuration provided here. It’s the result of thoughtful design and iteration over time.

* **Profiles**:

    * Define at least two profiles: `dev` and `prod`.
    * You can configure them using the `pom.xml` and the corresponding `application-<profile>.properties` files.

* **Testing Environment**:

    * In the `dev` profile (`application-dev.properties`), use the **H2 in-memory database** instead of PostgreSQL or MySQL.
    * This allows for faster development iterations—starting, restarting, and resetting both the system and database is quick and easy.

* **Database Initialization for Tests**:

    * Use `.json` files to initialize the test database.
    * Refer to
      `backend/src/main/java/br/jus/trf1/sjba/contavinculada/CommandLineAppStartupRunner.java`
      to see how initialization is handled.

* **H2 Access Configuration**:

    * Configure web access to the H2 database for debugging and inspection.
    * See
      `backend/src/main/java/br/jus/trf1/sjba/contavinculada/security/WebSecurityConfig.java`
      for details on how to set up access properly.

# Dependencies

- Postgress SQL or MYSQL


# Quick running with different profiles

To run without configuring the deployment environment, 
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```


```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

The prod profile should be used only when deploying the system.


# Deploy

On root project level, where are the pom.xml file, run, for generate the jar:
```
$mvn clean install -DskiptTests -Pprod
```
Update the content of the files <b>prod-users.json</b> and <b>setup_terceirizados.csv</b> to have the most updated information. Move those files to the same path of .jar file.
On the same level of .jar file, with the run the following command, with the values updated. Put the correct value for <b>DSERFI_CONTAS_DB_URL, DSERFI_CONTAS_DB_USER, DSERFI_CONTAS_DB_PASSWORD, DTECHNICAL_EMAILS and DSERFI_CONTAS_JWT_KEY</b>.

````
$java -DSERFI_CONTAS_DB_URL=jdbc:mysql://localhost:3307/conta_vinculada_db?prepareThreshold=0 -DSERFI_CONTAS_DB_USER=datbase_user -DSERFI_CONTAS_DB_PASSWORD=password -DTECHNICAL_EMAILS=technical-email1@email.com,technicalemail2@email.com -DSERFI_CONTAS_JWT_KEY=they-key-for-jwt-can-be-random-generation -jar contas-vinculadas.jar
````


Switch between installed java:
```
sudo update-alternatives --config java
```
