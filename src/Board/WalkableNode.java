package Board;

import Board.BoardNode;
import Actor.Actor;

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
        super.activate(a);
    }

    @Override
    public String toString() {
        String s = super.toString();
        if(s.equals(" ") && !hasWalked) {
            return ".";
        }
        return s;
    }
}
