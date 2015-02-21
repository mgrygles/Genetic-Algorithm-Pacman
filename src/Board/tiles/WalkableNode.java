package board.tiles;

import actors.Actor;
import actors.Ghost;
import actors.Player;

/**
 * Created by ahanes on 2/16/15.
 */
public class WalkableNode extends BoardNode {

    private boolean hasWalked;

    public WalkableNode(int x, int y) {
        super(x, y);
        this.hasWalked = false;
    }

    @Override
    public boolean canWalk(Actor a) {
        return true;
    }

    public void activate(Actor a) {
        //TODO Handle player
        if(a instanceof Player)
            this.hasWalked = true;
        super.activate(a);
    }

    @Override
    public String toString() {
        return (this.hasWalked) ? " " : ".";
    }
}
