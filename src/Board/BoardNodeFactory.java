package Board;

/**
 * Created by ahanes on 2/16/15.
 */
public class BoardNodeFactory {
    private BoardNodeFactory() {}

    public static BoardNode makeBoardNode(Character c, int x, int y) {
        switch (c) {
            case 'X':
                return new WallNode(x, y);
            case ' ':
                return new WalkableNode(x, y);
            case 'S':
                return new GhostSpawnNode(x, y);
            case 'P':
                return new PlayerSpawnNode(x, y);
            default:
                //TODO Use other node types
                return new WalkableNode(x, y);
        }
    }
}
