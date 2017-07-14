package pl.edu.pwr.wordnetloom.client.systems.common;

public class Quadruple<TA, TB, TC, TD> implements Container {

    private final TA a;
    private final TB b;
    private final TC c;
    private final TD d;

    public Quadruple(TA a, TB b, TC c, TD d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Object get(int index) {
        switch (index) {
            case 0:
                return a;
            case 1:
                return b;
            case 2:
                return c;
            case 3:
                return d;
        }
        return null;
    }

    public TA getA() {
        return a;
    }

    public TB getB() {
        return b;
    }

    public TC getC() {
        return c;
    }

    public TD getD() {
        return d;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getValue(int index) {
        switch (index) {
            case 0:
                return a;
            case 1:
                return b;
            case 2:
                return c;
            case 3:
                return d;
        }
        return null;
    }
}
