FROM memoriaio/java-docker:latest

ENV jarfile=io.memoria-recipes-app-0.0.1.jar

ADD app/target/${jarfile} /sources/

EXPOSE 8090
CMD java --enable-preview -jar ${jarfile}
