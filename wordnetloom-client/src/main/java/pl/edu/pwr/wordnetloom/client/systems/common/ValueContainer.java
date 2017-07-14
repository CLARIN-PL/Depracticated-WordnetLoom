package pl.edu.pwr.wordnetloom.client.systems.common;

public class ValueContainer<T> {

    private T value;

    public ValueContainer(T init) {
        this.value = init;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
