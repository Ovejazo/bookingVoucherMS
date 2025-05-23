FROM openjdk:17
ARG JAR_FILE=./target7*.jar
COPY ${JAR_FILE} bookingVoucherMS.jar
ENTRYPOINT ["java", "-jar", "/bookingVoucherMS.jar"]