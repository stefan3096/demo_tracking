# Use an official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build context
COPY target/*.jar app.jar

# Expose the port your app runs on (adjust if not 8080)
EXPOSE 8080
ENV JAVA_OPTS="-Xms1g -Xmx2g"
# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
