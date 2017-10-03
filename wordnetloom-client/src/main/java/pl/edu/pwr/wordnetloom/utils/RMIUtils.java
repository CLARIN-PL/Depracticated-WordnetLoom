package pl.edu.pwr.wordnetloom.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import pl.edu.pwr.wordnetloom.service.NativeServiceRemote;
import pl.edu.pwr.wordnetloom.workbench.implementation.PanelWorkbench;

public class RMIUtils {

	private static volatile Context initialContext;

	private static final String EJB_PROPERTIES = "config/server/jboss-ejb-client.properties";

	@SuppressWarnings("unchecked")
	public static <E> E lookupForService(Class<E> remoteClass) {
		Context context = null;
		E bean = null;
		try {
			context = RMIUtils.getInitialContext();
			String lookupName = getLookupName(remoteClass, "Bean");
			bean = (E) context.lookup(lookupName);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		E proxy = null;
		final E bbean = bean;
		if (remoteClass == NativeServiceRemote.class)
			return bean;

		proxy = (E) Proxy.newProxyInstance(remoteClass.getClassLoader(), new Class<?>[] { remoteClass },
				(proxy1, method, args) -> {
                    if (PanelWorkbench.getOwnerFromConfigManager() != null) {
                        if (!method.getName().toLowerCase().contains("get")) {
                            RemoteUtils.nativeRemote.setOwner(PanelWorkbench.getOwnerFromConfigManager());
                        }
                    }
                    return method.invoke(bbean, args);
                });
		return proxy;
	}

	public static Context getInitialContext() throws NamingException, IOException {
		if (initialContext == null) {
			synchronized (RMIUtils.class) {
				if (initialContext == null) {
					Properties ejbProperties = new Properties();
					InputStream ejbInputStream = new FileInputStream(EJB_PROPERTIES);
					ejbProperties.load(ejbInputStream);
					ejbProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
					ejbProperties.put("jboss.naming.client.ejb.context", "true");

					final EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(
							ejbProperties);
					final ConfigBasedEJBClientContextSelector selector = new ConfigBasedEJBClientContextSelector(
							ejbClientConfiguration);

					EJBClientContext.setSelector(selector);
					initialContext = new InitialContext(ejbProperties);
				}
			}
		}
		return initialContext;
	}

	public static <T> String getLookupName(Class<T> remoteClass, String beanName) {
		final String slash = "/";
		final String localBeanName = remoteClass.getSimpleName().replace("Remote", beanName);
		final String interfaceName = remoteClass.getName();
		final String appName = "wordnetloom-server-2.0";
		final String moduleName = "wordnetloom-service-2.0";
		final String distinctName = "";
		StringBuilder name = new StringBuilder();
		name.append("ejb:").append(appName).append(slash).append(moduleName).append(slash).append(distinctName)
				.append(slash).append(localBeanName).append("!").append(interfaceName);

		return name.toString();
	}
}
