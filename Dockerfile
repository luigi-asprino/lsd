FROM maven:3.8.6-openjdk-11

RUN cd /opt && \
	git clone https://github.com/luigi-asprino/lsd.git && \
    chmod +x /opt/lsd/mvnw

WORKDIR /opt/lsd

EXPOSE 8080

ENTRYPOINT ["mvnw", "spring-boot:run"]