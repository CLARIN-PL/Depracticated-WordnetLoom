package pl.edu.pwr.wordnetloom.client.utils;

import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

public class RMIUtils {

    private static volatile Context initialContext;

    @SuppressWarnings("unchecked")
    public static <E> E lookupForService(Class<E> remoteClass) {
        Context context;
        E bean = null;
        try {
            context = RMIUtils.getInitialContext();
            String lookupName = getLookupName(remoteClass, "Bean");
            bean = (E) context.lookup(lookupName);
        } catch (NamingException | IOException ex) {
            Logger.getLogger(RMIUtils.class).log(Level.ERROR, "Service Lookup error:", ex);
        }
        return bean;
    }

    private static Context getInitialContext() throws NamingException, IOException {
        Context localContext = RMIUtils.initialContext;
        if (localContext == null) {
            synchronized (RMIUtils.class) {
                localContext = RMIUtils.initialContext;
                if (localContext == null) {

                    String host = System.getProperty("wordnetloom.server.host") != null ? System.getProperty("wordnetloom.server.host") : "127.0.0.1";
                    String port = System.getProperty("wordnetloom.server.port") != null ? System.getProperty("wordnetloom.server.host") : "8080";

                    Properties ejbProperties = new Properties();
                    ejbProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
                    ejbProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
                    ejbProperties.put("jboss.naming.client.ejb.context", "true");
                    ejbProperties.put("remote.connections", "default");
                    ejbProperties.put("remote.connection.default.host", host);
                    ejbProperties.put("remote.connection.default.port", port);
                    ejbProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
                    ejbProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
                    ejbProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
                    ejbProperties.put("remote.connection.default.connect.options.org.xnio.Options.SSL_STARTTLS", "false");
                    ejbProperties.put("remote.connection.default.username", "wordnet");
                    ejbProperties.put("remote.connection.default.password", "123");

                    final EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(ejbProperties);
                    final ConfigBasedEJBClientContextSelector selector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
                    EJBClientContext.setSelector(selector);
                    RMIUtils.initialContext = localContext = new InitialContext(ejbProperties);
                }
            }
        }
        return localContext;
    }

    private static <T> String getLookupName(Class<T> remoteClass, String beanName) {
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
}
