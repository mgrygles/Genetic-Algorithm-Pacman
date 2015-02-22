package board.tiles;

import actors.Actor;

import java.awt.*;
import java.util.Random;

/**
 * Created by ahanes on 2/16/15.
 */
public class WallNode extends BoardNode {
    public WallNode(int x, int y) {
        super(x, y);
        Random rng = new Random();
        this.fgColor = new Color(0,0,255);
        this.bgColor = new Color(0,0,255);
    }

    @Override
    public boolean canWalk(Actor a) {
        return false;
    }

    @Override
    public String toString() {
        return "X";
    }

    @Override
    public Character getBoardCharacter() {
        return 'X';
    }
}
