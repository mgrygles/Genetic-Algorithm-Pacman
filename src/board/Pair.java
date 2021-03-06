package board;

/**
 * Created by ahanes on 3/14/15.
 */
public class Pair<T> {
    public T x, y;

    public Pair(T x, T y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (x != null ? !x.equals(pair.x) : pair.x != null) return false;
        if (y != null ? !y.equals(pair.y) : pair.y != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
