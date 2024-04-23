FROM openjdk:17
WORKDIR /app/wisefee
ARG JAR_PATH=../build/libs
COPY ${JAR_PATH}/*.jar /app/wisefee/test.jar
ENV PROFILE dev
ENTRYPOINT ["java", "-jar", "test.jar"]
