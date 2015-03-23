package board.tiles;

import board.BoardDrawable;
import board.Pair;
import board.actors.Actor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ahanes on 2/15/15.
 */
public abstract class BoardNode implements BoardDrawable {

    public HashMap<Pair, BoardNode> map = new HashMap<Pair, BoardNode>();
    protected Color fgColor;
    protected Color bgColor;
    private int x;
    private int y;
    private List<Actor> actors;

    public BoardNode(int x, int y) throws DuplicateCoordinateException {
        this.x = x;
        this.y = y;
        actors = new LinkedList<Actor>();
        this.fgColor = Color.WHITE;
        this.bgColor = Color.BLACK;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
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

    @Override
    public Color getForegroundColor() {
        return this.fgColor;
    }

    @Override
    public Color getBackgroundColor() {
        return this.bgColor;
    }

    protected class DuplicateCoordinateException extends RuntimeException {
        public DuplicateCoordinateException(String s) {
            super(s);
        }
    }


}
