package pl.edu.pwr.wordnetloom.client.workbench.interfaces;

import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;

import javax.swing.*;
import java.util.Collection;

/**
 * Interfejs zapewaniający dostęp do funkcji oferowanych przez perspektywę
 * <p>
 * NIE NALEŻY IMPLEMENTOWAĆ BEZPOŚREDNIO A WYKORZYSTYWAĆ AbstractPerspective,
 * które uproszcza tworzenie perspektyw
 *
 * @author Max
 */
public interface Perspective {

    /**
     * Odczytanie zawartości okna perspektywy czyli układu poszczególnych
     * splitterów
     *
     * @return okno perspektywy
     */
    JComponent getContent();

    /**
     * Odczytanie nazwy perspektywy
     *
     * @return - nazwa perspektywy
     */
    String getName();

    /**
     * Instalajca konktregnego widoku na konktrenej pozycji w perspektywie.
     * Znaczenia pola pos jest uzaleźnione od implementacji perspektywy, ale
     * zazwyczaj oznacza numer okna.
     *
     * @param view - widok
     * @param pos  - numer okienka
     */
    void installView(View view, int pos);

    /**
     * Inicjacja perpektywy, sprowadza się do utworzenia wszystkich sliterów.
     * Metoda jest wywoływana przez workbench w momencie instalacji perspektywy
     */
    void init();

    /**
     * Wymowałeni odświeżenia zawartości perspektywy w tym wszystkich jej okien
     */
    void refreshViews();

    /**
     * Przywrocenie domyslnych ustawien poszczególnych widokow, tj. rozłożenia
     * poszczególnych okienek, czyli w praktyce przywrócenie pozycji splitterów
     */
    void resetViews();

    /**
     * Odczytanie skrótow klawiaturowych przypisanych do poszczególnych widoków
     * w ramach jednek persepktywy. Są to skróty postacji Ctrl+1,Ctrl+2 które
     * umożliwiają szybkie przemieszczanie się pomiędzy poszczególnymi widokami
     * w ramach jednej perspektywy. Metoda ta jest wywoływana przez workbench.
     *
     * @return lista skrótów
     */
    Collection<ShortCut> getShortCuts();

    /**
     * Odczytanie stanu perseptkywy czyli tego co jest zaznaczone, np jakie
     * synsety czy jednosti. Tutaj można zwracać właściwie dowolne obiektu a
     * później w setState sprawdzać jakiego są typu i podejmować odpowiednie
     * akcje
     *
     * @return stan perpektywy
     */
    Object getState();

    /**
     * Ustawienie stanu perspektywy. Jako parametr dostaje jakiś obiekt albo
     * tablice z danymi od innej perspektywy. Jesli dane te sa kompatybilne
     * wtedy można je wykorzystać.
     *
     * @param state - nowy stan obiektu
     * @return TRUE jesli dane zostaly wykorzystane, FALSE jesli nic byly
     * poprawne
     */
    boolean setState(Object state);
}
