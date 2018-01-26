package pl.edu.pwr.wordnetloom.client.remote;

import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import pl.edu.pwr.wordnetloom.client.plugins.login.data.UserSessionData;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.service.UserServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

public class RemoteConnectionProvider implements Loggable {

    private static RemoteConnectionProvider instance;

    private Context initialContext;

    private UserSessionData userSessionData;

    private RemoteConnectionProvider() {
    }

    public static RemoteConnectionProvider getInstance() {
        if (instance == null) {
            synchronized (RemoteConnectionProvider.class) {
                if (instance == null) {
                    instance = new RemoteConnectionProvider();
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
            final EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(getEjbProperties());
            final ConfigBasedEJBClientContextSelector selector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
            EJBClientContext.setSelector(selector);
            initialContext = localContext = new InitialContext(getEjbProperties());
        }
        return localContext;
    }

    private Properties getEjbProperties() {

        String host = System.getProperty("wordnetloom.server.host") != null ? System.getProperty("wordnetloom.server.host") : "127.0.0.1";
        String port = System.getProperty("wordnetloom.server.port") != null ? System.getProperty("wordnetloom.server.host") : "8080";

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
        ejbProperties.put("remote.connection.default.username", userSessionData.getUsername());
        ejbProperties.put("remote.connection.default.password", userSessionData.getPassword());
        return ejbProperties;
    }

    private <T> String getLookupName(Class<T> remoteClass, String beanName) {
        String slash = "/";
        String localBeanName = remoteClass.getSimpleName().replace("Remote", beanName);
        final String interfaceName = remoteClass.getName();
        String appName = "wordnetloom-server-2.0";
        String moduleName = "wordnetloom-service-2.0";
        String distinctName = "";
        StringBuilder name = new StringBuilder();
        name.append("ejb:")
                .append(appName).append(slash)
                .append(moduleName).append(slash)
                .append(distinctName).append(slash)
                .append(localBeanName).append("!")
                .append(interfaceName);
        return name.toString();
    }

    public void setUserSessionData(UserSessionData data) {
        userSessionData = data;
    }

    public  UserSessionData getUserSessionData(){
        return userSessionData;
    }

    public User getUser() {
        if (userSessionData != null && userSessionData.getUser() == null) {
            User u = lookupForService(UserServiceRemote.class).findUserByEmail(userSessionData.getUsername());
            userSessionData = new UserSessionData(userSessionData, u);
        }
        return userSessionData.getUser();
    }

    public String getLanguage() {
        return userSessionData.getLanguage();
    }

    public void destroyInstance() {
        initialContext = null;
    }
}
