import board.Board;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;
import board.actors.geneticplayer.Chromosome;
import javafx.util.Pair;
import ui.PacmanUI;

import javax.swing.tree.TreePath;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static TreeMap<Double, Chromosome> fame = new TreeMap<>();

    public static List<Chromosome> firstPop(int count) {
        List<Chromosome> l = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            l.add(new Chromosome());
        }
        return l;
    }

    public static void drawGame(Chromosome gTree) throws Exception {
        Board board = Board.simpleBoard();
        board.Debug = true;
        Player p = new GeneticAlgorithmPlayer(board, gTree);
        board.registerActor(p);
        p.spawn(board.getPlayerSpawn());
        PacmanUI u = new PacmanUI(board);
        while (!board.isOver()) {
            board.boardTick();
            u.redrawGrid(board);
            Thread.sleep(100);
        }
        System.out.println("Game Over");
        System.out.printf("Score: %d\n", p.getScore());
    }

    public static List<Chromosome> run(List<Chromosome> trees) throws Exception {
        ExecutorService pool = new ThreadPoolExecutor(24, 36, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        ArrayList<Board> plays = new ArrayList<>(trees.size());
        for (Chromosome g : trees) {
                pool.execute(() -> {
                    g.score = 0;
                    for (int i = 0; i < 10; ++i) {
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
        for(Chromosome x : trees) {
            if(max < x.score) {
                max = x.score;
            }
            avg += x.score;
        }
        System.out.println("Average = " + avg/trees.size());
        System.out.println("Max = " + max);
        return trees;
    }

    public static List<Chromosome> mate(List<Chromosome> stuff) {
        List<Chromosome> n = new LinkedList<Chromosome>(stuff.subList(0, stuff.size()/10));
        List<Chromosome> babies = new LinkedList<Chromosome>(n);
        int ptr = 1;
        while(babies.size() != stuff.size()) {
            if(ptr >= n.size()) {
                ptr = 1;
            }
            int b = Board.rng.nextInt(ptr);
            babies.add(new Chromosome(n.get(ptr), n.get(b)));
            babies.get(babies.size() - 1).mutate();
            ++ptr;
        }
        return babies;
    }

    public static void main(String[] args) throws Exception {
        if(args.length < 2) {
            System.err.println("Usage: count gens");
        }
        int count = Integer.parseInt(args[0]);
        int gens = Integer.parseInt(args[1]);
        int famecount = 5;
        List<Chromosome> l = firstPop(count);
        System.out.printf("Running with %d pacmen at %d generations\n", count, gens);
        for (int i = 0; i < gens; ++i) {
            System.out.println("Generation " + i);
            if (i != 0) {
                mate(l);
            }
            new ArrayList<>(l.subList(0, famecount)).forEach(x -> fame.put(x.score, x));
            ArrayList<Map.Entry<Double, Chromosome>> best = new ArrayList<>();
            for(int i2 = 0; i2 < famecount; ++i2) {
                if(!fame.isEmpty()) {
                    Map.Entry<Double, Chromosome> e = fame.lastEntry();
                    fame.remove(e.getKey());
                    l.add(e.getValue());
                    best.add(e);
                }
            }
            fame = new TreeMap<>();
            best.forEach(x -> fame.put(x.getKey(), x.getValue()));
            l = run(l);
            Collections.sort(l, (x, y) -> new Double(y.score).compareTo(x.score));
            System.out.println(l.get(0).toString());
            l.forEach(x -> fame.put(x.score, x));
        }
        System.out.println(l.get(0).toString());
        System.out.println("Hit enter to play games");
        new Scanner(System.in).nextLine();
        for(int i = 0; i < 4; ++i)
            drawGame(fame.lastEntry().getValue());
    }
}
