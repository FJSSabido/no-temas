# Imagen base con Java 17
FROM eclipse-temurin:17-jdk

# Directorio de trabajo
WORKDIR /app

# Copiamos el proyecto
COPY . .

# Construimos la aplicación
RUN ./mvnw clean package -DskipTests

# Exponemos el puerto que usa Spring
EXPOSE 8080

# Arrancamos la app
CMD ["java", "-jar", "target/*.jar"]
