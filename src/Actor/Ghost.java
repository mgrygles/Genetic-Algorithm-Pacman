package Actor;

import Board.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by ahanes on 2/19/15.
 */
public class Ghost extends Actor {
    private int velocity = 0;
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
        if(choices.size() > 1 && choices.contains(this.last)) {
            choices.remove(this.last); // Don't go backwards
        }
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
    public void collision(Actor other) {

    }

    @Override
    public String toString() {
        return "H";
    }
}
