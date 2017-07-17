package pl.edu.pwr.wordnetloom.client.systems.misc;

import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import javax.swing.JFrame;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Tools {

    private static final String CHARSET = "UTF-8";

    static public void forceParentDirectory(String file) {
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
    static public int[] collectionToArray(Collection<Integer> list) {
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
     * @param file - plik
     * @param charset - kodowanie
     * @return liczba wierszy
     */
    static public int getFileLines(File file, Charset charset) {
        BufferedReader reader = null;
        int lines = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            while (reader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            Logger.getLogger(Tools.class).log(Level.ERROR, "Openig file " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Logger.getLogger(Tools.class).log(Level.ERROR, "Closing file" + e);
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
    static public String loadTemplate(String fileName) {
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
            Logger.getLogger(Tools.class).log(Level.ERROR, "Closing file" + e);
        }
        return sb.toString();
    }

    /**
     * zapisanie danych do pliku
     *
     * @param file - plik
     * @param value - dane do zapisania
     * @return true jesli sie udalo
     */
    static public boolean saveFile(File file, String value) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(file, CHARSET);
            writer.print(value);
            writer.close();
            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            Logger.getLogger(Tools.class).log(Level.ERROR, "Saving file" + e);
            return false;
        }
    }

    /**
     * Find parent frame of the container
     *
     * @param c - container
     * @return parent frame or null
     */
    public static JFrame findFrame(Container c) {
        if (c == null) {
            return null;
        }
        if (c instanceof JFrame) {
            return (JFrame) c;
        }
        return findFrame(c.getParent());
    }
}
