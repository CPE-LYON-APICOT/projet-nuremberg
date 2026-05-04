FROM gradle:8.8-jdk21

WORKDIR /app

COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY src ./src

EXPOSE 8080

CMD ["gradle", "runApi", "--no-daemon"]