
FROM gradle:jdk17-jammy AS builder


WORKDIR /app


# Copy the application source code
COPY . .

#Build the application
# RUN ./gradlew clean build

RUN gradle clean bootJar


FROM gradle:jdk17-jammy AS runtime

# Set the working directory in the container
WORKDIR /app

#Copy the built JAR from the builder stage to the final image
COPY --from=builder /app/build/libs/exoMatter-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port that the Spring Boot app runs on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
