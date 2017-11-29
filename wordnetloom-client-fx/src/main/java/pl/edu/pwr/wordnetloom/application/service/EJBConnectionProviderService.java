package pl.edu.pwr.wordnetloom.application.service;

import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.pwr.wordnetloom.application.utils.Loggable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

@Service
public class EJBConnectionProviderService implements Loggable {

    private Context initialContext;

    private String login;
    private String password;

    @Value("${service.host}")
    private String host;

    @Value("${service.port}")
    private String port;

    public <E> E lookupForService(Class<E> remoteClass) {
        E bean = null;
        try {
            Context context = getInitialContext();
            String lookupName = getLookupName(remoteClass, "Bean");
            bean = (E) context.lookup(lookupName);
        } catch (Exception ex) {
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
        Properties ejbProperties = new Properties();
        ejbProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        ejbProperties.put("service.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        ejbProperties.put("jboss.naming.client.ejb.context", "true");
        ejbProperties.put("service.connections", "default");
        ejbProperties.put("service.connection.default.host", host);
        ejbProperties.put("service.connection.default.port", port);
        ejbProperties.put("service.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        ejbProperties.put("service.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        ejbProperties.put("service.connection.default.connect.options.org.xnio.Options.SSL_STARTTLS", "false");
        ejbProperties.put("service.connection.default.username", login);
        ejbProperties.put("service.connection.default.password", password);
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

    public void setLoginAndPassword(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void destroyInstance() {
        setLoginAndPassword(null, null);
        initialContext = null;
    }
}
