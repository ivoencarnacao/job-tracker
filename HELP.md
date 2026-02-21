# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

- [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.11/maven-plugin)
- [Create an OCI image](https://docs.spring.io/spring-boot/3.5.11/maven-plugin/build-image.html)
- [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/3.5.11/reference/testing/testcontainers.html#testing.testcontainers)
- [Testcontainers Postgres Module Reference Guide](https://java.testcontainers.org/modules/databases/postgres/)
- [Spring Web](https://docs.spring.io/spring-boot/3.5.11/reference/web/servlet.html)
- [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.11/reference/data/sql.html#data.sql.jpa-and-spring-data)
- [Flyway Migration](https://docs.spring.io/spring-boot/3.5.11/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
- [Docker Compose Support](https://docs.spring.io/spring-boot/3.5.11/reference/features/dev-services.html#features.dev-services.docker-compose)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.11/reference/using/devtools.html)
- [Validation](https://docs.spring.io/spring-boot/3.5.11/reference/io/validation.html)
- [Testcontainers](https://java.testcontainers.org/)
- [Spring Security](https://docs.spring.io/spring-boot/3.5.11/reference/web/spring-security.html)
- [Thymeleaf](https://docs.spring.io/spring-boot/3.5.11/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)

### Guides

The following guides illustrate how to use some features concretely:

- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Validation](https://spring.io/guides/gs/validating-form-input/)
- [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
- [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
- [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)

### Docker Compose support

This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

- postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)

Please review the tags of the used images and set them to the same as you're running in production.

### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/3.5.11/reference/features/dev-services.html#features.dev-services.testcontainers).

Testcontainers has been configured to use the following Docker images:

- [`postgres:latest`](https://hub.docker.com/_/postgres)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

# Live reload setup

This project uses Vite to have live reloading.

Use the following steps to get it working:

1. Start the Vite development server with `npm run dev`.
2. Run the Spring Boot application with the `local` profile. You can do this from your IDE,
   or via the command line using `mvn spring-boot:run -Dspring-boot.run.profiles=local`.
3. Open your browser at http://localhost:8080

You should now be able to change any HTML or CSS and have the browser reload upon saving the file.

PS: It is also possible to use the URL that Vite uses (Usually http://localhost:5173) given the
Spring Boot application runs on port 8080. If another port is used, you will need to edit `vite.config.js`.
