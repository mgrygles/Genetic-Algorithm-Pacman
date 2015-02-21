package Actor;

import Board.Board;
import Board.BoardNode;

import java.awt.*;
import java.util.Random;

/**
 * Created by ahanes on 2/15/15.
 */
public abstract class Actor {
    protected boolean active;
    protected BoardNode location;
    protected Board board;
    protected Color color;

    protected Actor(Board board) {
        this.active = false;
        this.location = null;
        Random rng = new Random();
        this.color = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
    }

    public Color getColor() {
        return color;
    }

    public BoardNode getLocation() {
        return location;
    }

    protected void setLocation(BoardNode location) {
        this.location = location;
    }

    public void remove() {
        this.active = false;
        this.location = null;
    }

    public void die() {
        this.remove();
    }

    public boolean onBoard() {
        return this.location != null;
    }

    public abstract BoardNode move();

    public void spawn(BoardNode start) {
        this.setLocation(start);
        this.setActive(true);
    }

    public abstract void spawn();

    public abstract void collision(Actor other);

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public abstract String toString();
}
