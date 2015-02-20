package Board;

/**
 * Created by ahanes on 2/19/15.
 */
public class PlayerSpawnNode extends WalkableNode {

    public PlayerSpawnNode(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "S";
    }
}
