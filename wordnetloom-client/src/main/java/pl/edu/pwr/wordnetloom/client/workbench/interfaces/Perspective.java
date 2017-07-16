package pl.edu.pwr.wordnetloom.client.workbench.interfaces;

import java.util.Collection;
import javax.swing.JComponent;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;

/**
 * Interfejs zapewaniający dostęp do funkcji oferowanych przez perspektywę
 *
 * NIE NALEŻY IMPLEMENTOWAĆ BEZPOŚREDNIO A WYKORZYSTYWAĆ AbstractPerspective,
 * które uproszcza tworzenie perspektyw
 *
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
    public JComponent getContent();

    /**
     * Odczytanie nazwy perspektywy
     *
     * @return - nazwa perspektywy
     */
    public String getName();

    /**
     * Instalajca konktregnego widoku na konktrenej pozycji w perspektywie.
     * Znaczenia pola pos jest uzaleźnione od implementacji perspektywy, ale
     * zazwyczaj oznacza numer okna.
     *
     * @param view - widok
     * @param pos - numer okienka
     */
    public void installView(View view, int pos);

    /**
     * Inicjacja perpektywy, sprowadza się do utworzenia wszystkich sliterów.
     * Metoda jest wywoływana przez workbench w momencie instalacji perspektywy
     */
    public void init();

    /**
     * Wymowałeni odświeżenia zawartości perspektywy w tym wszystkich jej okien
     */
    public void refreshViews();

    /**
     * Przywrocenie domyslnych ustawien poszczególnych widokow, tj. rozłożenia
     * poszczególnych okienek, czyli w praktyce przywrócenie pozycji splitterów
     */
    public void resetViews();

    /**
     * Odczytanie skrótow klawiaturowych przypisanych do poszczególnych widoków
     * w ramach jednek persepktywy. Są to skróty postacji Ctrl+1,Ctrl+2 które
     * umożliwiają szybkie przemieszczanie się pomiędzy poszczególnymi widokami
     * w ramach jednej perspektywy. Metoda ta jest wywoływana przez workbench.
     *
     * @return lista skrótów
     */
    public Collection<ShortCut> getShortCuts();

    /**
     * Odczytanie stanu perseptkywy czyli tego co jest zaznaczone, np jakie
     * synsety czy jednosti. Tutaj można zwracać właściwie dowolne obiektu a
     * później w setState sprawdzać jakiego są typu i podejmować odpowiednie
     * akcje
     *
     * @return stan perpektywy
     */
    public Object getState();

    /**
     * Ustawienie stanu perspektywy. Jako parametr dostaje jakiś obiekt albo
     * tablice z danymi od innej perspektywy. Jesli dane te sa kompatybilne
     * wtedy można je wykorzystać.
     *
     * @param state - nowy stan obiektu
     * @return TRUE jesli dane zostaly wykorzystane, FALSE jesli nic byly
     * poprawne
     */
    public boolean setState(Object state);
}
