package pl.edu.pwr.wordnetloom.client.remote;

import com.google.common.eventbus.Subscribe;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.security.PasswordChangedEvent;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

public class ConnectionProvider implements Loggable {

    private static ConnectionProvider instance;

    private Context initialContext;

    private String username;
    private String password;

    private ConnectionProvider() {
        Application.eventBus.register(this);
    }

    public static ConnectionProvider getInstance() {
        if (instance == null) {
            synchronized (ConnectionProvider.class) {
                if (instance == null) {
                    instance = new ConnectionProvider();
                }
            }
        }
        return instance;
    }

    public <E> E lookupForService(Class<E> remoteClass) {
        E bean = null;
        try {
            Context context = getInitialContext();
            String lookupName = getLookupName(remoteClass, "Bean");
            bean = (E) context.lookup(lookupName);
        } catch (NamingException | IOException ex) {
            logger().error("Service Lookup error:", ex);
        }
        return bean;
    }

    private Context getInitialContext() throws NamingException, IOException {
        Context localContext = initialContext;
        if (localContext == null) {
            EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(getEjbProperties());
            ConfigBasedEJBClientContextSelector selector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
            EJBClientContext.setSelector(selector);
            initialContext = localContext = new InitialContext(getEjbProperties());
        }
        return localContext;
    }

    private Properties getEjbProperties() {

        String host = System.getenv("WORDNETLOOM_SERVER_HOST") != null ? System.getenv("WORDNETLOOM_SERVER_HOST") : "127.0.0.1";
        String port = System.getenv("WORDNETLOOM_SERVER_PORT") != null ? System.getenv("WORDNETLOOM_SERVER_PORT") : "8080";

        Properties ejbProperties = new Properties();
        ejbProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        ejbProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        ejbProperties.put("jboss.naming.client.ejb.context", "true");
        ejbProperties.put("remote.connections", "default");
        ejbProperties.put("remote.connection.default.host", host);
        ejbProperties.put("remote.connection.default.port", port);
        ejbProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        ejbProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        ejbProperties.put("remote.connection.default.connect.options.org.xnio.Options.SSL_STARTTLS", "false");
        ejbProperties.put("remote.connection.default.username", username);
        ejbProperties.put("remote.connection.default.password", password);
        return ejbProperties;
    }

    private <T> String getLookupName(Class<T> remoteClass, String beanName) {
        String slash = "/";
        String localBeanName = remoteClass.getSimpleName().replace("Remote", beanName);
        String interfaceName = remoteClass.getName();
        String appName = "wordnetloom-server-2.0";
        String moduleName = "wordnetloom-service-2.0";
        String distinctName = "";
        String name = "ejb:" +
                appName + slash +
                moduleName + slash +
                distinctName + slash +
                localBeanName + "!" +
                interfaceName;
        return name;
    }

    public void setCredentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    @Subscribe
    public void onPasswordChange(PasswordChangedEvent event){
        this.password = event.getPassword();
    }
    public void destroyInstance() {
        initialContext = null;
    }
}
