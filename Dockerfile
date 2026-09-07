# ==========================================
# Stage 1: Build React Frontend
# ==========================================
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY quiz-frontend/package*.json ./
RUN npm ci
COPY quiz-frontend/ ./
RUN npm run build

# ==========================================
# Stage 2: Build Spring Boot JAR with embedded Frontend
# ==========================================
FROM eclipse-temurin:17-jdk-jammy AS backend-builder
WORKDIR /app/backend
COPY quiz-backend/.mvn .mvn
COPY quiz-backend/mvnw quiz-backend/pom.xml ./
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

COPY quiz-backend/src ./src
# Embed compiled frontend assets into Spring Boot static directory
COPY --from=frontend-builder /app/frontend/dist/ ./src/main/resources/static/
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 3: Production JRE 17 Runtime
# ==========================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=backend-builder /app/backend/target/*.jar app.jar

# Render automatically supplies the PORT environment variable
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
