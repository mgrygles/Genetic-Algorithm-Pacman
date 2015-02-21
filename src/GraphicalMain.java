import actors.Actor;
import actors.DumbPlayer;
import actors.Ghost;
import board.Board;
import ui.PacmanUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahanes on 2/21/15.
 */
public class GraphicalMain {
    public static void main(String[] args) throws Exception {
        Board board = new Board(new File("board.txt"));
        List<Actor> actors = new ArrayList<Actor>();
        for (int i = 0; i < 6; ++i) {
            Actor a = new Ghost(board);
            actors.add(new Ghost(board));
            board.registerActor(a);
            a.spawn(board.getGhostSpawn());
        }
        DumbPlayer p = new DumbPlayer(board);
        p.addInvuln(5);
        actors.add(p);
        board.registerActor(p);
        p.spawn(board.getPlayerSpawn());
        PacmanUI u = new PacmanUI(board);
        while (!board.isOver()) {
            board.boardTick();
            u.redrawGrid(board);
            Thread.sleep(200);
        }
        System.out.println("Game Over");
        System.out.printf("Score: %d\n", p.getScore());
    }
}
