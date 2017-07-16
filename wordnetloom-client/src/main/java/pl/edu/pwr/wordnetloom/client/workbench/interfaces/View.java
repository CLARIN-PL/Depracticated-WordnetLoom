package pl.edu.pwr.wordnetloom.client.workbench.interfaces;

import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JPanel;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;

/**
 * Uniwersalny interfejs zapewniający dostęp do funkcji oferowanych przez widok
 *
 * NIE NALEŻY IMPLEMENTOWAĆ BEZPOŚREDNIO A WYKORZYSTYWAĆ AbstractView, które
 * uproszcza użycie widoków
 *
 * @author <a href="mailto:lukasz.jastrzebski@pwr.wroc.pl">Lukasz
 * Jastrzebski</a>
 */
public interface View {

    /**
     * Odczytanie tytułu konkretnego widoku
     *
     * @return tytuł widoku
     */
    String getTitle();

    /**
     * Odczytanie panelu czyli głownego (nadrzędnego) obszaru roboczego widoku.
     * Wszystkie elementy instalowane są właśnie w tym panelu.
     *
     * @return panel
     */
    JPanel getPanel();

    /**
     * Odczytanie zapamiętanych skrótów dla przycisków
     *
     * @return lista skrotow
     */
    Collection<ShortCut> getShortCuts();

    /**
     * Odczytanie głównego komponentu okna, na którym focus jest ustawiany przy
     * przechodzeniu pomiędzy widokami za pomocą kombinacji klawiszy
     * Ctrl+1,Ctrl+2, itp
     *
     * @return komponent
     */
    JComponent getRootComponent();
}
