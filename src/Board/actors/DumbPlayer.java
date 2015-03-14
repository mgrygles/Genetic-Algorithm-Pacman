package board.actors;

import board.Board;
import board.tiles.BoardNode;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ahanes on 2/19/15.
 */
public class DumbPlayer extends Player {
    private Random rng;
    private BoardNode last;

    public DumbPlayer(Board b) {
        super(b);
        this.rng = Board.rng;
        this.last = null;
    }

    @Override
    public BoardNode move() {
        this.tick();
        List<BoardNode> choices = this.getLocation().getNeighbors(this);
        if (choices.size() > 1 && choices.contains(this.last)) {
            choices.remove(this.last); // Don't go backwards
        }
        this.last = this.getLocation();
        Collections.shuffle(choices, Board.rng);
        this.setLocation(choices.get(0));
        return this.getLocation();
    }

    public void die() {
        board.gameOver();
    }

    @Override
    public void spawn(BoardNode start) {
        super.spawn(start);
        this.last = start;
    }

    @Override
    public void spawn() {
        this.spawn(this.board.getGhostSpawn());
    }


    @Override
    public String toString() {
        return "<";
    }

}
