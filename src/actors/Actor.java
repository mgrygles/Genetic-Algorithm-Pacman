package actors;

import board.Board;
import board.tiles.BoardNode;

import java.awt.*;
import java.util.Random;

/**
 * Created by ahanes on 2/15/15.
 */
public abstract class Actor {
    protected boolean active;
    protected Board board;
    protected Color color;
    protected int score;
    private BoardNode location;

    protected Actor(Board board) {
        this.board = board;
        this.active = false;
        this.location = null;
        Random rng = new Random();
        this.color = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
        this.score = 0;
    }

    public Color getColor() {
        return color;
    }

    public BoardNode getLocation() {
        return location;
    }

    protected void setLocation(BoardNode location) {
        location.activate(this);
        this.location = location;
    }

    public void remove() {
        this.active = false;
    }

    public void die() {
        this.remove();
        this.board.getSpawnQueue().add(this, 15);
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

    public void incrScore() {
        this.score++;
    }

    public void incrScore(int n) {
        this.score += n;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public abstract String toString();
}
