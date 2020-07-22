FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY qiaoqiaochat-web/target/*.jar web.jar
COPY qiaoqiaochat-websocket/target/*.jar websocket.jar
COPY run.sh run.sh
RUN chmod +x /run.sh
CMD [ "sh","/run.sh" ]

