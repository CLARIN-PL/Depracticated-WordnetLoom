package pl.edu.pwr.wordnetloom.client.systems.misc;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;

public class Tools implements Loggable {

    private static final String CHARSET = "UTF-8";
    private static volatile Tools instance = null;

    private Tools() {
    }

    public static Tools getInstance() {
        if (instance == null) {
            synchronized (Tools.class) {
                instance = new Tools();
            }
        }
        return instance;
    }

    public void forceParentDirectory(String file) {
        File root = new File(file); // bezpieczniejsza wersja
        if (root != null) {
            File parent = root.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
        }
    }

    /**
     * konwersja kolekcji na tablice
     *
     * @param list - lista elementow
     * @return tablica elementow
     */
    public int[] collectionToArray(Collection<Integer> list) {
        int[] array = new int[list.size()]; // konwersja na tablice
        int index = 0;
        for (Integer i : list) { // przejscie po wszystkich elementach
            array[index++] = i.intValue();
        }
        return array;
    }

    /**
     * odczytanie liczby wierszy pliku tekstowego
     *
     * @param file    - plik
     * @param charset - kodowanie
     * @return liczba wierszy
     */
    public int getFileLines(File file, Charset charset) {
        BufferedReader reader = null;
        int lines = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            while (reader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            logger().error("Openig file " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger().error("Closing file", e);
                }
            }
        }
        return lines;
    }

    /**
     * wczytanie pliku wzoraca
     *
     * @param fileName - nazwa pliku
     * @return zawartośc pliku
     */
    public String loadTemplate(String fileName) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), CHARSET));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            logger().error("Closing file", e);
        }
        return sb.toString();
    }

    /**
     * zapisanie danych do pliku
     *
     * @param file  - plik
     * @param value - dane do zapisania
     * @return true jesli sie udalo
     */
    public boolean saveFile(File file, String value) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(file, CHARSET);
            writer.print(value);
            writer.close();
            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            logger().error("Saving file", e);
            return false;
        }
    }

    /**
     * Find parent frame of the container
     *
     * @param c - container
     * @return parent frame or null
     */
    public JFrame findFrame(Container c) {
        if (c == null) {
            return null;
        }
        if (c instanceof JFrame) {
            return (JFrame) c;
        }
        return findFrame(c.getParent());
    }
}
