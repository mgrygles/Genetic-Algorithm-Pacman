package board.tiles;

import actors.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ahanes on 2/15/15.
 */
public abstract class BoardNode {

    private static final HashMap<Pair, BoardNode> map = new HashMap<Pair, BoardNode>();
    private int x;
    private int y;
    private List<Actor> actors;

    public BoardNode(int x, int y) throws DuplicateCoordinateException {
        this.x = x;
        this.y = y;
        actors = new LinkedList<Actor>();
        //if(this.map.containsKey(new Pair(x, y))) {
        //   throw new DuplicateCoordinateException(String.format("Coordinates taken: %d, %d", x, y));
        //}
        this.map.put(new Pair(x, y), this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<BoardNode> getNeighbors(Actor a) {
        //Up
        List<BoardNode> l = new ArrayList<BoardNode>(4);
        List<Pair<Integer>> opts = new LinkedList<Pair<Integer>>();
        opts.add(new Pair<Integer>(this.x - 1, this.y));
        opts.add(new Pair<Integer>(this.x + 1, this.y));
        opts.add(new Pair<Integer>(this.x, this.y + 1));
        opts.add(new Pair<Integer>(this.x, this.y - 1));
        for (Pair<Integer> p : opts) {
            if (this.map.containsKey(p)) {
                BoardNode b = this.map.get(p);
                if (b.canWalk(a)) {
                    l.add(b);
                }
            }
        }
        return l;
    }

    public void activate(Actor a) {
        this.actors.add(a);
    }

    public void deactivate() {
        this.actors.clear();
    }

    public void tick() {
        for (Actor a : this.actors) {
            for (Actor b : this.actors) {
                if (a != b) {
                    a.collision(b);
                }
            }
        }
    }

    @Override
    public String toString() {
        if (this.actors.isEmpty()) {
            return " ";
        }
        return this.actors.get(0).toString();
    }

    public abstract boolean canWalk(Actor a);

    protected class DuplicateCoordinateException extends RuntimeException {
        public DuplicateCoordinateException(String s) {
            super(s);
        }
    }

    protected class Pair<T> {
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
}