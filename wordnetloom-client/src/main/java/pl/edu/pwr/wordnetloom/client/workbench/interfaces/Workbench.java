package pl.edu.pwr.wordnetloom.client.workbench.interfaces;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Interfejs zapewniający dostęp do funkcji oferowanych przez workbench
 *
 * @author ehlyast
 * @author Max
 */
public interface Workbench {

    /**
     * Ustawienie widoczności głównego okna aplikacji
     *
     * @param isVisible - TRUE okno jest widoczne, FALSE nie jest schowane
     */
    void setVisible(boolean isVisible);

    /**
     * Ustawienei widiczoności paska narzędziowego na którym znajdują się
     * przyciski do zmiany perspektywy
     *
     * @param isVisible - TRUE okno jest widoczny, FALSE nie
     */
    void setToolbarVisible(boolean isVisible);

    /**
     * Odczytanie widocznosci paska
     *
     * @return TRUE jesli widoczny
     */
    boolean isToolbarVisible();

    /**
     * Instalacja nowego menu w workbench. Pozycja jest dodawana na końcu
     * aktualnie istniejących. Jeśli zainstalowane jest już menu "Plik" i menu
     * "Edycja" to nowe menu zostanie dodane na prawo od "Edycja"
     *
     * @param item - menu do zainstalowania
     */
    void installMenu(JMenu item);

    /**
     * Install menu on given position. If index is negative the position is
     * counted from the end. -1 is the last position.
     *
     * @param item
     * @param index
     */
    void installMenu(JMenu item, int index);

    void installMenu(String topMenu, String subMenu, JMenuItem item);

    /**
     * Odczytanie menu o konkretnej nazwie. Przeszukiwana jest lista
     * zainstalowanych menu i zwracane jest konkretne lub NULL
     *
     * @param itemName - nazwa menu do odczytania
     * @return menu lub NULL gdy nie znaleziono
     */
    JMenu getMenu(String itemName);

    /**
     * Instalacja kontkretnego widoku, na konktretnej pozycji w konkretnej
     * perspektywie
     *
     * @param view - obiekt reprezentujący widok
     * @param pos - pozycja w widoku, znaczenia jest uzaleznione od
     * implementacji perspektywy
     * @param perspectiveName - nazwa perspektywy, w której widok ma zostać
     * zainstalowany
     */
    void installView(View view, int pos, String perspectiveName);

    /**
     * Instalacja perspektywy w środowisku
     *
     * @param perspective - perspektywa do instalacji
     */
    void installPerspective(Perspective perspective);

    /**
     * odczytanie aktualnej perspektywy
     *
     * @return aktualna perspektywa
     */
    Perspective getActivePerspective();

    /**
     * Instalacja usługi dostarczającej jakiejś funkcjonalności poprzez np.
     * widoki lub elementy w menu.
     *
     * @param service - usługa do zainstalowania
     */
    void installService(Service service);

    /**
     * Odczytanie usługi o konktretnej nazwie
     *
     * @param name - nazwa usługi
     * @return obiekt z usługą lub NULL
     */
    Service getService(String name);

    /**
     * Odczytanie określonego parametru z listy przechowywanej przez środowisko.
     * Parametry sa ładowane z pliku konfiguracyjnego podczas startu aplikacji.
     *
     * @param paramName - nazwa parametru
     * @return przechowywana wartość
     */
    String getParam(String paramName);

    /**
     * Zapisanie określonego parametru do środowiska
     *
     * @param paramName - nazwa parametru
     * @param value - nowa wartość
     */
    void setParam(String paramName, String value);

    /**
     * Usuniecie parametru z konfiguracji
     *
     * @param paramName - nazwa parametru
     */
    void removeParam(String paramName);

    /**
     * Odczytanie wersji programu
     *
     * @return wersja programu
     */
    String getVersion();

    /**
     * Wybranie perspektywy o określonej nazwie jako aktywnej. Spowoduje to
     * przełączenie odpowiednich widoków.
     *
     * @param perspectiveName - nazwa perspektywy
     */
    void choosePerspective(String perspectiveName);

    /**
     * Ustawienie tekstu dla paska statusu
     *
     * @param text - tekst dla statusu
     */
    void setStatusText(String text);

    /**
     * Odczytanie głównego kontenera okna na którym wszystko bazuje. Jest to
     * wykorzystywane przy oknach dialogowych
     *
     * @return nadrzędny komponent
     */
    JFrame getFrame();

    void setBusy(boolean busy);

    void updateOwner();

}
