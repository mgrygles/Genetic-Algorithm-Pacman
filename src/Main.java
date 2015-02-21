import actors.Actor;
import actors.Ghost;
import actors.Player;
import board.Board;
import ui.PacmanUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, Exception {
        Board board = new Board(new File("board.txt"));
        List<Actor> actors = new ArrayList<Actor>();
        for (int i = 0; i < 10; ++i) {
            Actor a = new Ghost(board);
            actors.add(new Ghost(board));
            board.registerActor(a);
            a.spawn(board.getGhostSpawn());
        }
        Player p = new Player(board);
        actors.add(p);
        board.registerActor(p);
        p.spawn(board.getPlayerSpawn());
        PacmanUI u = new PacmanUI(board);
        while(!board.isOver()) {
            board.boardTick();
            u.redrawGrid(board);
            Thread.sleep(100);
        }
        System.out.println("Game Over");
    }
}
