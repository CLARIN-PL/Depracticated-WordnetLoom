# WordnetLoom
Wordnet Visual Editor

# Wildfly configuration

Security config

```
<security-realm name="ApplicationRealm">
	<server-identities>
		<ssl>
	                <keystore path="application.keystore" relative-to="jboss.server.config.dir" keystore-password="password" alias="server" key-password="password" generate-self-signed-certificate-host="localhost"/>
                </ssl>
	</server-identities>
        <authentication>
                <jaas name="wordnetloom"/>
        </authentication>
</security-realm>

<security-domain name="wordnetloom" cache-type="default">
	<authentication>
	        <login-module code="Database" flag="required">
	                <module-option name="dsJndiName" value="java:/datasources/wordnet"/>
                        <module-option name="principalsQuery" value="SELECT password FROM users WHERE email=?"/>
                        <module-option name="rolesQuery" value="SELECT u.role, 'Roles' FROM users u WHERE u.email=?"/>
                        <module-option name="password-stacking" value="useFirstPass"/>
                        <module-option name="hashAlgorithm" value="SHA-256"/>
                        <module-option name="hashEncoding" value="base64"/>
                </login-module>
	</authentication>
</security-domain>
```
Database config

```
<datasource jta="true" jndi-name="java:/datasources/wordnet" pool-name="WordnetDS" enabled="true" use-ccm="true">
	<connection-url>jdbc:mysql://localhost:3306/wordnet</connection-url>
	        <driver-class>com.mysql.jdbc.Driver</driver-class>
                <connection-property name="useUnicode">
                        true
                </connection-property>
                <connection-property name="characterEncoding">
                        UTF-8
                </connection-property>
                <connection-property name="useSSL">
                        false
                </connection-property>
                <driver>mysql</driver>
                <security>
                        <user-name>XXXXXXX</user-name>
                        <password>XXXXXXXX</password>
                </security>
                <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                        <background-validation>true</background-validation>
                        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
                </validation>
</datasource>
<drivers>
	<driver name="mysql" module="com.mysql.driver">
	        <driver-class>com.mysql.jdbc.Driver</driver-class>
        </driver>
</drivers>
```