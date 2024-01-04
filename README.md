# Ktor-Koin-Example &nbsp;&nbsp;&nbsp;&nbsp; English (Current Page)  |  [简体中文](https://github.com/BongleXD/ktor-koin-example/blob/main/README-zh-cn.md)
This is a sample project that demonstrates how to use [Ktor](https://ktor.io), [Google KSP](https://github.com/google/ksp), and [Koin](https://insert-koin.io/docs/reference/koin-ktor/ktor/), to create a simple RESTful API.

## What is Ktor?
Ktor is a framework for building asynchronous servers and clients in connected systems using the powerful Kotlin programming language. Ktor provides a DSL (domain-specific language) to easily configure and customize your application. You can use Ktor to create RESTful APIs, web sockets, static content, authentication, and more.

## What is Koin?
Koin is a pragmatic dependency injection framework for Kotlin developers. Koin uses a simple and concise DSL or annotations (KSP) to declare and inject dependencies, Koin supports Android, Ktor, and Kotlin Multiplatform projects.

## What is Google KSP?
Google KSP (Kotlin Symbol Processing) is a library that provides an API to build lightweight compiler plugins. KSP analyzes Kotlin code directly, which is up to 2x faster than using annotation processors. KSP also has a better understanding of Kotlin's language constructs, such as sealed classes, inline classes, and generics.

## Features
- Ktor server with Netty engine
- Ktor annotation controller
- Koin dependency injection
- KSP annotation processor
- JWT authentication
- JSON serialization and deserialization with Jackson
- Logging with SLF4J (@Slf4j KSP Annotation)
- Data access with Exposed ORM

## Structure
The project has the following structure:

- `src/main/kotlin` contains the Kotlin source code for the application.
- `src/main/resources` contains the application configuration files.
- `processor` contains the annotations and symbol processors for Google KSP.
- `build/generated/ksp` contains the generated data classes from Google KSP.

## Endpoints

The project exposes the following endpoints:

- `POST /login`: User login.
- `POST /register`: User register.

## How to run

To run the project, you need to have JDK 17 or higher installed.

To run the project from the command line, use the following commands:

```bash
# Clone the repository
git clone https://github.com/BongleXD/ktor-koin-example.git

# Change directory to the project folder
cd ktor-koin-example

# Run the application
./gradlew run
```

To run the project from IntelliJ IDEA, import the project as a Gradle project and run the `Application.kt` file.

The application will start on port 8080. You can access it from your browser at http://localhost:8080.

## License

This project is licensed under the Apache-2.0 License. See the [LICENSE](LICENSE) file for details.
