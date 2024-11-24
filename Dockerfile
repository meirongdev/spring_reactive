# 使用 Maven 构建阶段
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# 将项目的 pom.xml 和源代码复制到容器中
COPY pom.xml .
COPY src ./src

# 使用 Maven 构建项目，指定 Profile
RUN mvn clean package -Pwebflux-helloworld -DskipTests

# 使用 OpenJDK 运行阶段
FROM openjdk:17-jdk-slim
WORKDIR /app

# 从构建阶段复制生成的 JAR 文件到运行阶段
COPY --from=build /app/target/webflux-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用程序的端口
EXPOSE 8080
EXPOSE 9010 

# 运行应用程序，并启用 JMX
ENTRYPOINT ["java", "-Dcom.sun.management.jmxremote", \
            "-Dcom.sun.management.jmxremote.port=9010", \
            "-Dcom.sun.management.jmxremote.rmi.port=9010", \
            "-Dcom.sun.management.jmxremote.authenticate=false", \
            "-Dcom.sun.management.jmxremote.ssl=false", \
            "-Djava.rmi.server.hostname=0.0.0.0", \
            "-jar", "app.jar"]