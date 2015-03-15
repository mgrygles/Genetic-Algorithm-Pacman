import board.Board;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;
import board.actors.geneticplayer.GeneticTree;
import ui.PacmanUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static List<GeneticTree> firstPop(int count) {
        List<GeneticTree> l = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            l.add(new GeneticTree());
        }
        return l;
    }

    public static void drawGame(GeneticTree gTree) throws Exception {
        Board board = Board.simpleBoard();
        board.Debug = true;
        Player p = new GeneticAlgorithmPlayer(board, gTree);
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

    public static List<GeneticTree> run(List<GeneticTree> trees) throws Exception {
        ConcurrentHashMap<GeneticTree, Integer> scores = new ConcurrentHashMap<>();
        ExecutorService pool = new ThreadPoolExecutor(4, 12, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        ArrayList<Board> plays = new ArrayList<>(trees.size());
        for (GeneticTree g : trees) {
            scores.put(g, 0);
            for (int i = 0; i < 10; ++i) {
                Board b = Board.simpleBoard();
                GeneticAlgorithmPlayer ga = new GeneticAlgorithmPlayer(b, g);
                ga.spawn(b.getPlayerSpawn());
                b.registerActor(ga);
                plays.add(b);
                pool.execute(() -> {
                    b.play();
                    scores.put(g, scores.get(g) + b.getPlayer().getScore());
                });
            }
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
        }
        LinkedList<GeneticTree> ts = new LinkedList<>(trees);
        ts.sort((x, y) -> scores.get(y).compareTo(scores.get(x)));
        return ts;
    }

    public static List<GeneticTree> mate(List<GeneticTree> stuff) {
        List<GeneticTree> elite = new ArrayList<GeneticTree>(stuff.size());
        for (int i = 0; i < stuff.size() / 2; ++i) {
            int rand = Board.rng.nextInt(i + 10);
            int rand2 = Board.rng.nextInt(i + 25);
            elite.add(new GeneticTree(stuff.get(i), stuff.get(rand)));
            elite.add(new GeneticTree(stuff.get(i), stuff.get(rand2)));
        }
        return elite;
    }

    public static void main(String[] args) throws Exception {
        int count = 200;
        List<GeneticTree> l = firstPop(count);
        for (int i = 0; i < 5; ++i) {
            System.out.println("Generation " + i);
            if (i != 0)
                mate(l);
            l = run(l);
        }
        drawGame(l.get(0));
    }
}
