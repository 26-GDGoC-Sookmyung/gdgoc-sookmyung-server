FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew \
    && ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar -x test --no-daemon \
    && cp build/libs/website-*.jar build/libs/app.jar

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/app.jar app.jar

ENV TZ=Asia/Seoul
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Duser.timezone=Asia/Seoul -jar app.jar --server.port=${PORT}"]