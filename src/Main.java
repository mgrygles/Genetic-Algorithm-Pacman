import board.Board;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;
import board.actors.geneticplayer.GeneticTree;
import ui.PacmanUI;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        ExecutorService pool = new ThreadPoolExecutor(4, 12, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        ArrayList<Board> plays = new ArrayList<>(trees.size());
        for (GeneticTree g : trees) {
                pool.execute(() -> {
                    g.score = 0;
                    for (int i = 0; i < 5; ++i) {
                        Board b = null;
                        try {
                            b = Board.simpleBoard();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        GeneticAlgorithmPlayer ga = new GeneticAlgorithmPlayer(b, g);
                        ga.spawn(b.getPlayerSpawn());
                        b.registerActor(ga);
                        plays.add(b);
                        long count = b.play();
                        g.score += b.getPlayer().getScore()/((float)count);
                    }
                });
        }
        pool.shutdown();
        while (!pool.isTerminated()) {}
        double avg = 0;
        double max = 0;
        for(GeneticTree x : trees) {
            if(max < x.score) {
                max = x.score;
            }
            avg += x.score;
        }
        //System.out.println("Average = " + avg/trees.size());
        //System.out.println("Max = " + max);
        return trees;
    }

    public static List<GeneticTree> mate(List<GeneticTree> stuff) {
        List<GeneticTree> n = new LinkedList<GeneticTree>(stuff.subList(0, stuff.size()/5));
        List<GeneticTree> babies = new LinkedList<GeneticTree>(n);
        Collections.shuffle(stuff);
        while(babies.size() != stuff.size()) {
            int a = Board.rng.nextInt(n.size());
            int b = Board.rng.nextInt(n.size());
            babies.add(new GeneticTree(n.get(a), n.get(b)));
            babies.get(babies.size() - 1).mutate();
        }
        return babies;
    }

    public static void main(String[] args) throws Exception {
        int count = 20;
        int gens = 25;
        List<GeneticTree> l = firstPop(count);
        System.out.printf("Running with %d pacmen at %d generations\n", count, gens);
        for (int i = 0; i < gens; ++i) {
            System.out.println("Generation " + i);
            if (i != 0) {
                mate(l);
            }
            l = run(l);
            Collections.sort(l, (x, y) -> (int)(y.score - x.score));
        }
        System.out.println(l.get(0).toString());
        System.out.println("Hit next to play games");
        new Scanner(System.in).nextLine();
        for(int i = 0; i < 4; ++i)
        drawGame(l.get(0));
    }
}
