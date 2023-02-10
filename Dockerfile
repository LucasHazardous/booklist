FROM openjdk:19
ADD build/libs/booklist-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java","-jar","/app.jar"]