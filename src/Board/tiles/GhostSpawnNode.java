package board.tiles;

/**
 * Created by ahanes on 2/19/15.
 */
public class GhostSpawnNode extends WalkableNode {
    public GhostSpawnNode(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "G";
    }
}
