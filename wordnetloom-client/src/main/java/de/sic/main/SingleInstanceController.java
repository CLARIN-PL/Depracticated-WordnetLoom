package de.sic.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * SingleInstanceController is a small java library, which enables you to check, if your application
 * is already runing on the system. In this case, you can communicate with the older application. 
 * This library works application and port dependent. <br>
 * <br>
 * The library tests, if an other instance of the same application is running. In this case, 
 * you can communicate with the running application, otherwise the library tries to find a free 
 * port. If a free port was found, the library starts a ServerSocket at this port and writes the 
 * port number in a specified directory in order to notify later started applications about the 
 * chosen port number.
 * 
 * @author Stefan Kiesel
 * @since 1.5
 * @version 1.0
 */
public class SingleInstanceController {

	private boolean result = false;
	private File file = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private ServerSocket server = null;
	private Socket client = null;
	private ArrayList<ApplicationStartedListener> listener = null;
	private String appname = null;

	/**
	 * Creates a new SingleInstanceController with a spefic application name (this name is 
	 * importend to compare the running applications. Try to use an unique name for each 
	 * application) and a location, where the used port number should be/is saved.
	 * @param file port location
	 * @param appname unique identification for each application (not instance)
	 * @see #SingleInstanceController(String)
	 */
	public SingleInstanceController(File file, String appname) {

		this.file = file;
		this.appname = appname;
		listener = new ArrayList<ApplicationStartedListener>();
	}

	/**
	 * Creates a new SingleInstanceController without a spefic location to save the port number.
	 * You should better use {@link #SingleInstanceController(File, String)}, because any application
	 * uses by default this path.
	 * @param appname unique identification for each application (not instance)
	 * @see #SingleInstanceController(File, String)
	 */
	public SingleInstanceController(String appname) {
		this(new File(System.getProperty("java.io.tmpdir") + "/923jhakE53Kk9235b43.6m7"), appname);
	}

	/**
	 * Adds an ApplicationStartedListener
	 * @param asl the ApplicationStartedListener
	 * @see #removeApplicationStartedListener(ApplicationStartedListener)
	 */
	public void addApplicationStartedListener(ApplicationStartedListener asl) {
		listener.add(asl);
	}

	/**
	 * Removes an ApplicationStartedListener
	 * @param asl the ApplicationStartedListener
	 * @see #addApplicationStartedListener(ApplicationStartedListener)
	 */
	public void removeApplicationStartedListener(ApplicationStartedListener asl) {
		listener.remove(asl);
	}

	/**
	 * Checks, if an other instance of this application is already running on the system
	 * @return true if it is sure, that an other instance is running. Otherwise false
	 */
	public boolean isOtherInstanceRunning() {

		if (!file.exists()) {
			return false;
		}
		return sendMessageToRunningApplication(new ClassCheck(appname));
	}

	/**
	 * Sends a message to the first started instance. Before calling this method, ensure that
	 * an other instance is running.
	 * @param obj the object, you would like to send
	 * @return true if the object was sended successful
	 * @see #isOtherInstanceRunning()
	 */
	public boolean sendMessageToRunningApplication(final Object obj) {

		result = false;
		try {
			client = new Socket("localhost", getPortNumber());
			return true;
		}
		catch (IOException e) {
			result = false;
		}
		return result;
	}

/**
 * Registers this application as started. All later started instances can communicate with
 * this instance. You can register the instance when an other instance is still running, too.
 * @return true if the application was successful registered
 */
public boolean registerApplication() {

	try {
		if (!file.exists()) {
			if (!file.getParentFile().mkdirs() && !file.getParentFile().exists()) {
				return false;
			}
			if (!file.createNewFile()) {
				return false;
			}
		}
		BufferedWriter wuffy = new BufferedWriter(new FileWriter(file));
		int port = getFreeServerSocket();
		if (port != -1) {
			startServer();
		}
		wuffy.write(String.valueOf(port));
		wuffy.close();
		return true;
	}
	catch (IOException e) {
		return false;
	}
}

/**
 * Notifies the listeners, that an incomming message arrived
 * @param obj Objekt fuer die Listener
 */
protected void messageArrived(Object obj) {

	for (ApplicationStartedListener asl : listener) {
		asl.messageArrived(obj);
	}
}

/**
 * Notifies the listeners, that an other instance of this application was started and successful
 * connected to this instance.
 */
protected void applicationStartet() {

	for (ApplicationStartedListener asl : listener) {
		asl.applicationStarted();
	}
}

/**
 * Notifies the listeners, that an other application tried to connect to this port.
 */
protected void foreignApplicationStarted(String name) {

	for (ApplicationStartedListener asl : listener) {
		asl.foreignApplicationStarted(name);
	}
}

/**
 * Detect the last set port number
 * @return the last set port number
 */
private int getPortNumber() {

	try {
		BufferedReader buffy = new BufferedReader(new FileReader(file));
		int port = Integer.parseInt(buffy.readLine().trim());
		buffy.close();
		return port;
	}
	catch (Exception e) {
		return -1;
	}
}

/**
 * Starts the server to communicate with other applications/instances
 */
private void startServer() {

	Thread t = new Thread(new Runnable() {
		public void run() {
			while (true) {
				try {
					client = server.accept();
					if (client.getInetAddress().getCanonicalHostName().equalsIgnoreCase("localhost")) {
						new Thread(new Runnable() {
							public void run() {
								try {
									oos = new ObjectOutputStream(client.getOutputStream());
									ois = new ObjectInputStream(client.getInputStream());
									Object obj = ois.readObject();
									if (obj instanceof ClassCheck) {
										if (obj.toString().equals(appname)) {
											oos.writeBoolean(true);
											applicationStartet();
										}
										else {
											oos.writeBoolean(false);
											foreignApplicationStarted(obj.toString());
										}
									}
									else {
										messageArrived(obj);
										oos.writeBoolean(true);
									}
									oos.flush();
									client.close();
								}
								catch (IOException e) {
									e.printStackTrace();
								}
								catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	});
	t.start();
}

/**
 * Detect a free port number between 30000 and 65535
 * @return the first free port or -1 if no port is free.
 */
private int getFreeServerSocket() {

	for (int i = 30000; i < 65535; i++) {
		try {
			server = new ServerSocket(i);
			return i;
		}
		catch (IOException ignore) {
		}
	}
	return -1;
}
}
