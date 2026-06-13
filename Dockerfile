# --- Этап 1: Сборка приложения ---
FROM gradle:8-jdk21 AS builder
WORKDIR /app

# Копируем файлы конфигурации Gradle для кэширования зависимостей
COPY gradlew .
COPY gradle gradle
COPY build.gradle* settings.gradle* ./

# Скачиваем зависимости (этот шаг закэшируется, если build.gradle не менялся)
RUN ./gradlew dependencies --no-daemon

# Копируем исходный код и собираем .jar архив
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# --- Этап 2: Запуск приложения ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Создаем системного пользователя для безопасности (не запускаем под root)
RUN useradd -ms /bin/bash springuser
USER springuser

# Копируем только готовый jar-файл из предыдущего этапа
COPY --from=builder /app/build/libs/*.jar app.jar

# Открываем порт (обычно 8080 для Spring Boot)
EXPOSE 8080

# Оптимальные параметры JVM для контейнеров
ENTRYPOINT ["java", "-XX:+UseG1GC", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]
