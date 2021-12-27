# To build, run the following command from the top level project directory:
#
# docker build -t dizitiveit/pmsapp .

# Docker run:
# docker run -d --name CRM -p 8080:8080 dizitiveit/pmsapp
# Docker application will start with 8080 port.

FROM adoptopenjdk/openjdk11:jre-11.0.6_10-ubuntu



EXPOSE 8080

RUN mkdir /app

COPY build/libs/*.jar /app/pms-application.jar

ENTRYPOINT ["java","-jar","/app/pms-application.jar"]