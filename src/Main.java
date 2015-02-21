import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import Actor.*;
import Board.*;
import UI.PacmanUI;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, Exception {
        Board board = new Board(new File("board.txt"));
        List<Actor> actors = new ArrayList<Actor>();
        for(int i = 0; i < 4; ++i) {
            Actor a = new Ghost(board);
            actors.add(new Ghost(board));
            board.registerActor(a);
            a.spawn(board.getGhostSpawn());
        }
        PacmanUI u = new PacmanUI(board);
        for(int i = 0; i < 10000; ++i) {
            board.boardTick();
            u.redrawGrid(board);
            Thread.sleep(100);
            System.out.println(board.toString());
        }
    }
}
