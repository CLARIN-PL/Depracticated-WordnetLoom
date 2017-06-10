package pl.edu.pwr.wordnetloom.client.systems.spantable;

/**
 * interfejs dla mappera połaczonych komputer
 *
 * @author Max
 */
public interface SpanMapperInterface {

    /**
     * odczytanie ilosci polaczonych komorek
     *
     * @param row - wiersz
     * @param column - kolumna
     * @return ilosc polaczonych komorek
     */
    int rowSpan(int row, int column);
}
