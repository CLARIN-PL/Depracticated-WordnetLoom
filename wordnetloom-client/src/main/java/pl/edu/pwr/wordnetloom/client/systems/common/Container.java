package pl.edu.pwr.wordnetloom.client.systems.common;

public interface Container {

    /**
     * konwertuje ity element z kontenera do postaci obiektu
     */
    public Object getValue(int index);

    /**
     * Stored elements count
     */
    public int getCount();
}
