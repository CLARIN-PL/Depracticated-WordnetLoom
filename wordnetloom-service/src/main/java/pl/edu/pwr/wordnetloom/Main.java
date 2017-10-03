package pl.edu.pwr.wordnetloom;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasa główna serwera.
 * Rozpoczyna komiunikację z JBoss'em
 */
public class Main {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	private static String log_filename = "plwordnet_exception "+dateFormat.format(new Date())+".log";
	
	/**
	 * Głowna metoda aplikacji
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("empty main");
	}
	
	/**
	 * Logowanie wyjątków do pliku
	 * @param ex
	 */
	static public void logException(Exception ex) {

		try {
			PrintWriter writer = new PrintWriter(new FileWriter(log_filename, true));
			writer.println();
			writer.println("================================================");
			writer.println("Exception @ "+dateFormat.format(new Date()));
			writer.println("------------------------------------------------");
	        ex.printStackTrace(writer);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        ex.printStackTrace();
	}
}
