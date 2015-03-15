import board.Board;
import board.actors.Actor;
import board.actors.DumbPlayer;
import board.actors.Ghost;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;
import ui.PacmanUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        int count = 10000;
        List<Board> boards = new ArrayList<Board>(count);
        List<GeneticAlgorithmPlayer> genetics = new ArrayList<GeneticAlgorithmPlayer>(count);
        BlockingQueue<Runnable> games = new LinkedBlockingQueue<Runnable>(count);
        ExecutorService exec = new ThreadPoolExecutor(4, 8, 10, TimeUnit.MINUTES, games);
        for (int i = 0; i < count; ++i) {
            Board b = new Board(new File("board.txt"));
            List<Actor> actors = new ArrayList<Actor>();
            for (int i2 = 0; i2 < 6; ++i2) {
                Actor a = new Ghost(b);
                actors.add(a);
                b.registerActor(a);
                a.spawn(b.getGhostSpawn());
            }
            GeneticAlgorithmPlayer gp = new GeneticAlgorithmPlayer(b);
            gp.spawn(b.getPlayerSpawn());
            b.registerActor(gp);
            genetics.add(gp);
            boards.add(b);
            exec.execute(() -> b.play());
        }
        exec.shutdown();
        while (!exec.isShutdown()) {}
    }
}
