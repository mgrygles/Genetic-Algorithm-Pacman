package Board;

import Actor.Actor;

/**
 * Created by ahanes on 2/16/15.
 */
public class WallNode extends BoardNode {
    public WallNode(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canWalk(Actor a) {
        return false;
    }

    @Override
    public String toString() {
        return "X";
    }
}
