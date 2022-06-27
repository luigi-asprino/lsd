FROM maven:3.8.6-openjdk-11

RUN cd /opt && \
	git clone https://github.com/luigi-asprino/lsd.git && \
    cd lsd && \
    mvn clean package

WORKDIR /opt/lsd

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/lsd/target/lsd-0.0.1-SNAPSHOT.jar"]