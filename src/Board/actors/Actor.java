package board.actors;

import board.Board;
import board.BoardDrawable;
import board.tiles.BoardNode;

import java.awt.*;
import java.util.Random;

/**
 * Created by ahanes on 2/15/15.
 */
public abstract class Actor implements BoardDrawable {
    protected boolean active;
    protected Board board;
    protected Color fgColor;
    protected int score;
    private BoardNode location;
    protected Color bgColor;

    @Override
    public Color getForegroundColor() {
        return this.fgColor;
    }

    @Override
    public Color getBackgroundColor() {
        return this.bgColor;
    }

    protected Actor(Board board) {
        this.board = board;
        this.active = false;
        this.location = null;
        Random rng = new Random();
        this.fgColor = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
        this.bgColor = Color.WHITE;
        this.score = 0;
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
