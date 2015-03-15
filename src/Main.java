import board.Board;
import board.actors.Actor;
import board.actors.DumbPlayer;
import board.actors.Ghost;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
        int count = 10;
        List<Board> boards = new ArrayList<Board>(count);
        List<GeneticAlgorithmPlayer> genetics = new ArrayList<GeneticAlgorithmPlayer>(count);
        BlockingQueue<Runnable> games = new LinkedBlockingQueue<Runnable>(count);
        ExecutorService exec = new ThreadPoolExecutor(4, 8, 5, TimeUnit.MINUTES, games);
        int best = 0;
        GeneticAlgorithmPlayer bestp;
        for(int i = 0; i < count; ++i) {
            GeneticAlgorithmPlayer gp = new GeneticAlgorithmPlayer(board);
            Board b = new Board(new File("board.txt"));
            gp.spawn(b.getPlayerSpawn());
            b.registerActor(gp);
            genetics.add(gp);
            boards.add(b);
            exec.execute(() -> b.play());
        }
        exec.shutdown();
        while(!exec.isShutdown()) {}
        for(int i = 0; i < 10; ++i) {
            for(Actor a : board.getActors()) {
                if (a instanceof GeneticAlgorithmPlayer) {
                    System.out.println(a.getScore());
                }
            }
        }
    }
}
