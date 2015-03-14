import board.Board;
import board.actors.Actor;
import board.actors.DumbPlayer;
import board.actors.Ghost;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Board board = new Board(new File("board.txt"));
        List<Actor> actors = new ArrayList<Actor>();
        for (int i = 0; i < 6; ++i) {
            Actor a = new Ghost(board);
            actors.add(new Ghost(board));
            board.registerActor(a);
            a.spawn(board.getGhostSpawn());
        }
        Player p = new GeneticAlgorithmPlayer(board);
        p.addInvuln(5);
        actors.add(p);
        board.registerActor(p);
        p.spawn(board.getPlayerSpawn());
        while (!board.isOver()) {
            board.boardTick();
        }
        System.out.println("Game Over");
        System.out.printf("Score: %d\n", p.getScore());
    }
}
