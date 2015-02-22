package board.actors;

import board.Board;
import board.tiles.BoardNode;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ahanes on 2/19/15.
 */
public class Ghost extends Actor {
    private Random rng;
    private BoardNode last;

    public Ghost(Board b) {
        super(b);
        this.rng = new Random();
        this.last = null;
    }

    @Override
    public BoardNode move() {
        List<BoardNode> choices = this.getLocation().getNeighbors(this);
        if (choices.size() > 1 && choices.contains(this.last)) {
            choices.remove(this.last); // Don't go backwards
        }
        this.last = this.getLocation();
        Collections.shuffle(choices);
        this.setLocation(choices.get(0));
        return this.getLocation();
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
    public void collision(Actor other) {
    }

    @Override
    public String toString() {
        return "H";
    }

    @Override
    public Character getBoardCharacter() {
        return 'H';
    }
}
