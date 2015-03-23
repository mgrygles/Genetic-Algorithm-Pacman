package board;

import board.tiles.*;

import java.util.HashMap;

/**
 * Created by ahanes on 2/16/15.
 */
public class BoardNodeFactory {
    private BoardNodeFactory() {
    }

    public static BoardNode makeBoardNode(HashMap<Pair, BoardNode> map, Character c, int x, int y) {
        BoardNode b;
        switch (c) {
            case 'X':
                b = new WallNode(x, y);
                break;
            case ' ':
                b = new WalkableNode(x, y);
                break;
            case '*':
                b = new WalkableNode(x, y, true);
                break;
            case 'S':
                b = new GhostSpawnNode(x, y);
                break;
            case 'P':
                b = new PlayerSpawnNode(x, y);
                break;
            default:
                b = new WalkableNode(x, y);
                break;
        }
        b.map = map;
        b.map.put(new Pair(x, y), b);
        return b;
    }
}
