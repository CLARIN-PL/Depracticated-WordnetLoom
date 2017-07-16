package pl.edu.pwr.wordnetloom.client.workbench.interfaces;

/**
 * Uniwersalny interfejs zapewniający dostęp do funkcji oferowanych przez usługę
 *
 * NIE NALEŻY IMPLEMENTOWAĆ BEZPOŚREDNIO A WYKORZYSTYWAĆ AbstractService, które
 * uproszcza tworzenie usług
 *
 * @author <a href="mailto:lukasz.jastrzebski@pwr.wroc.pl">Lukasz
 * Jastrzebski</a>
 */
public interface Service {

    /**
     * Odczytanie identyfikatora usługi, w praktyce jest to nazwa klasy
     *
     * @return identyfiikator usługi
     */
    String getId();

    /**
     * Metoda wywoływana w celu instalacji widoków należących do konktretnej
     * usługi
     */
    void installViews();

    /**
     * Metoda wywoływana w celu instalacji elementów menu należących do
     * konktretnej usłgi
     */
    void installMenuItems();

    /**
     * Metoda wywoływana celem sprawdzenia czy można zamknąć usługę najcześciej
     * wywoływana przy zamykaniu aplikacji. Może być wykorzystywana do
     * przypominana o archiwizacji
     *
     * @return czy można zamknąć serwis
     */
    boolean onClose();

    /**
     * Zdarzenie wywoływane przy uruchamianiu usług po instalacji odpowiednich
     * elementów w menu
     */
    void onStart();
}
