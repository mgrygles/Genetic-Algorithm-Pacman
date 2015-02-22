package board.tiles;

import actors.Actor;
import actors.Player;

import java.awt.*;

/**
 * Created by ahanes on 2/16/15.
 */
public class WalkableNode extends BoardNode {

    private boolean hasWalked;
    private boolean hasPowerup;
    private Color fgColor;
    private Color bgColor;

    public WalkableNode(int x, int y, boolean hasPowerup) {
        super(x, y);
        this.hasWalked = false;
        this.hasPowerup = true;
    }

    public WalkableNode(int x, int y) {
        super(x, y);
        this.hasWalked = false;
        this.hasPowerup = false;
    }

    @Override
    public boolean canWalk(Actor a) {
        return true;
    }

    public void activate(Actor a) {
        //TODO Handle player
        if (a instanceof Player && !this.hasWalked) {
            this.hasWalked = true;
            a.incrScore();
        }
        if (this.hasPowerup && a instanceof Player) {
            this.hasPowerup = false;
            ((Player) a).addInvuln(15);
        }
        super.activate(a);
    }

    @Override
    public String toString() {
        String r = (this.hasWalked) ? " " : ".";
        return (this.hasPowerup) ? "*" : r;
    }

    @Override
    public Character getBoardCharacter() {
        String r = (this.hasWalked) ? " " : ".";
        return ((this.hasPowerup) ? "*" : r).charAt(0);
    }
}
