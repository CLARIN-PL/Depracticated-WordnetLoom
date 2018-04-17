FROM clarinpl/java

# Maintainer
LABEL maintainer="Tomasz Naskręt, clarin-pl.eu" description="WordnetLoom  on WildFly 10 with MySql Drivers"
# Wildfly
ENV VERSION 10.1.0.Final
ENV INSTALL_DIR /opt
ENV WILDFLY_HOME ${INSTALL_DIR}/wildfly-${VERSION}
ENV DEPLOYMENT_DIR ${WILDFLY_HOME}/standalone/deployments/
ENV CONFIGURATION_DIR ${WILDFLY_HOME}/standalone/configuration
ENV WILDFLY_CLI $WILDFLY_HOME/bin/jboss-cli.sh
RUN useradd -b /opt -m -s /bin/sh -d ${INSTALL_DIR} serveradmin && echo serveradmin:serveradmin | chpasswd
RUN curl -O https://download.jboss.org/wildfly/${VERSION}/wildfly-${VERSION}.zip \
    && unzip wildfly-${VERSION}.zip -d ${INSTALL_DIR} \
    && rm wildfly-${VERSION}.zip \
    && chown -R serveradmin:serveradmin /opt \
    && chmod a+x ${WILDFLY_HOME}/bin/standalone.sh \
    && chmod -R a+rw ${INSTALL_DIR}
USER serveradmin
RUN rm ${WILDFLY_HOME}/bin/standalone.conf
ADD standalone.conf ${WILDFLY_HOME}/bin/

# Database
ENV MYSQL_VERSION 5.1.46
ENV DB_NAME wordnet
ENV DB_USER root
ENV DB_PASS password
ENV DB_URI 127.0.0.1:3306

# Configure Wildfly server
RUN rm ${WILDFLY_HOME}/standalone/configuration/standalone-full.xml
ADD standalone-full.xml ${WILDFLY_HOME}/standalone/configuration/

RUN echo "=> Starting WildFly server" && \
      bash -c '$WILDFLY_HOME/bin/standalone.sh -c standalone-full.xml &' && \
    echo "=> Waiting for the server to boot" && \
     sleep 6 && \
    echo "=> Downloading MySQL driver" && \
      curl --location --output /tmp/mysql-connector-java-${MYSQL_VERSION}.jar --url http://search.maven.org/remotecontent?filepath=mysql/mysql-connector-java/${MYSQL_VERSION}/mysql-connector-java-${MYSQL_VERSION}.jar && \
    echo "=> Adding MySQL module" && \
      $WILDFLY_CLI --connect --command="module add --name=com.mysql --resources=/tmp/mysql-connector-java-${MYSQL_VERSION}.jar --dependencies=javax.api,javax.transaction.api" && \
    echo "=> Adding MySQL driver" && \
      $WILDFLY_CLI --connect --command="/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-xa-datasource-class-name=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)" && \
    echo "=> Creating a new datasource" && \
            $WILDFLY_CLI --connect --command="data-source add \
              --name=${DB_NAME}DS \
              --jndi-name=java:/datasources/${DB_NAME} \
              --user-name=${DB_USER} \
              --password=${DB_PASS} \
              --driver-name=mysql \
              --connection-url=jdbc:mysql://${DB_URI}/${DB_NAME}?useSSL=false&useUnicode=true&characterEncoding=UTF-8 \
              --use-ccm=false \
              --max-pool-size=25 \
              --blocking-timeout-wait-millis=5000 \
              --enabled=true" && \
    echo "=> Shutting down WildFly and Cleaning up" && \
      $WILDFLY_CLI --connect --command=":shutdown" && \
      rm -rf $WILDFLY_HOME/standalone/configuration/standalone_xml_history/ $WILDFLY_HOME/standalone/log/* && \
      rm -f /tmp/*.jar


# Expose http and admin ports
EXPOSE 8081 9991

#echo "=> Restarting WildFly"
# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interfaces
## Deploying
COPY ./wordnetloom-server/target/wordnetloom-server-2.0.ear ${DEPLOYMENT_DIR}

ENTRYPOINT ${WILDFLY_HOME}/bin/standalone.sh -c standalone-full.xml -b=0.0.0.0 -bmanagment=0.0.0.0 -Djboss.http.port=8081 -Djboss.management.http.port=9991