package pl.edu.pwr.wordnetloom.client.systems.common;

import java.util.Objects;

public class Pair<TA, TB> implements Container {

    private final TA a;
    private final TB b;

    public Pair(TA a, TB b) {
        this.a = a;
        this.b = b;
    }

    public TA getA() {
        return a;
    }

    public TB getB() {
        return b;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getValue(int index) {
        switch (index) {
            case 0:
                return a;
            case 1:
                return b;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return a.hashCode() ^ b.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }
}
