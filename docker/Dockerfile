FROM qa.stratio.com/stratio/ubuntu-base:16.04
MAINTAINER Stratio Crossdata team "crossdata@stratio.com"

# USAGE: build --build-arg PKG=<crossdata-version> -t <docker-name> <output-path>
# USAGE Example: docker build --build-arg PKG=1.8.0-RC2-SNAPSHOT -t crossdata-enterprise .

COPY . /

VOLUME /usr/lib/mesos

ARG PKG

RUN wget -q "http://apt.repository.stratio.com/pool/trusty/1.7/main/stratio-release_1.0.0_all.deb" \
            && dpkg -i stratio-release_1.0.0_all.deb \
            && rm -rf stratio-release_1.0.0_all.deb \
            && apt-get update \
            && ./dependencyfix.sh stratio-crossdata-mesosphere-scala211-${PKG}.all.deb \
            && dpkg -i stratio-crossdata-mesosphere-scala211-${PKG}.all.deb
     
ENTRYPOINT ["/docker-entrypoint.sh"]
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-amd64
     
CMD tail -f /var/log/*

