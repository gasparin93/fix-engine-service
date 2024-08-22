FROM openjdk:22-jdk-slim
LABEL authors="gasparin93"

#RUN addgroup -S spring && adduser -S spring -G spring #alpine
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} fix-engine.jar
ENTRYPOINT ["java","-Xms6g","-Xmx6g","-XX:ActiveProcessorCount=4","-Djdk.defaultScheduler.parallelism=4","-XX:+UseZGC","-XX:+CompactStrings","-jar","/fix-engine.jar"]