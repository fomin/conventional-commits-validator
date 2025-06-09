FROM eclipse-temurin:21.0.7_6-jdk-noble AS builder

RUN apt-get update && apt-get install -y unzip \
  && rm -rf /var/lib/apt/lists/*

WORKDIR /project

COPY . .

# Build with BuildKit cache mounts for Gradle
RUN --mount=type=cache,target=/root/.gradle/wrapper \
    --mount=type=cache,target=/root/.gradle/caches \
    ./gradlew :validator-jre-cli:build --no-daemon
RUN mkdir -p /project/dist \
 && unzip /project/validator-jre-cli/build/distributions/validator-jre-cli-*.zip -d /project/dist \
 && cp -r /project/dist/validator-jre-cli-*/* /project/dist \
 && rm -rf /project/dist/validator-jre-cli-*

FROM eclipse-temurin:21.0.7_6-jre-noble AS runtime

WORKDIR /app

COPY --from=builder /project/dist .

ENTRYPOINT ["bin/validator-jre-cli"]
