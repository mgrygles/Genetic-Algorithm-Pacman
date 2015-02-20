import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import Actor.*;
import Board.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Board board = new Board(new File("board.txt"));
        List<Actor> actors = new ArrayList<Actor>();
        for(int i = 0; i < 4; ++i) {
            Actor a = new Ghost(board);
            actors.add(new Ghost(board));
            board.registerActor(a);
            assert(board.getGhostSpawn() != null);
            a.spawn(board.getGhostSpawn());
        }
        for(int i = 0; i < 100; ++i) {
            System.out.println(board.toString());
            board.boardTick();
        }
    }
}
