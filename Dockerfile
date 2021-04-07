FROM memoriaio/java-docker:latest

ENV jarfile=io.memoria.recipes-*.jar

ADD .github/settings.xml /root/.m2/settings.xml

RUN mkdir /recipes
WORKDIR /recipes
ADD app/ /recipes/app
ADD core/ /recipes/core
ADD pom.xml /recipes/pom.xml
RUN apt-get update && apt-get install -y apt-transport-https
RUN export JAVA_HOME=/sources/jdk \
    && export MAVEN_HOME=/sources/maven/bin \
    && export PATH=$PATH:$MAVEN_HOME:$JAVA_HOME/bin \
    && mvn install

EXPOSE 8090
CMD java --enable-preview -jar "/recipes/app/target/${jarfile}"
