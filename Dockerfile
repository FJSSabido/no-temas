FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

# 🔑 DAR PERMISOS AL WRAPPER
RUN chmod +x mvnw

# Construir la aplicación
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/*.jar"]
