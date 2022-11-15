FROM java:8

COPY *.jar /app.jar

CMD ["--server.port=8525"]

EXPOSE 8525

ENTRYPOINT ["java","-jar","/app.jar"]