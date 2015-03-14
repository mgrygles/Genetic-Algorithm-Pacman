package board.actors;

import board.Board;
import board.tiles.BoardNode;

import java.awt.*;

/**
 * Created by ahanes on 2/21/15.
 */
public abstract class Player extends Actor {
    protected int invulnTicks;

    public Player(Board board) {
        super(board);
        this.invulnTicks = 0;
        this.bgColor = Color.YELLOW;
    }

    public void tick() {
        if (this.invulnTicks > 0) {
            this.invulnTicks--;
        } else {
            this.invulnTicks = 0;
        }
    }

    public void addInvuln(int time) {
        this.invulnTicks += time;
    }

    public boolean invulnerable() {
        return this.invulnTicks > 0;
    }

    @Override
    public void collision(Actor other) {
        if (this.invulnerable()) {
            other.die();
            this.incrScore(100);
        } else {
            board.gameOver();
        }
    }

    @Override
    public void spawn(BoardNode start) {
        super.spawn(start);
    }

    @Override
    public Character getBoardCharacter() {
        return '<';
    }
}
