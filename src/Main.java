import board.Board;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;
import board.actors.geneticplayer.GeneticTree;
import ui.PacmanUI;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<GeneticTree> firstPop(int count) {
        List<GeneticTree> l = new ArrayList<>(count);
        for(int i = 0; i < count; ++i) {
            l.add(new GeneticTree());
        }
        return l;
    }

    public static void drawGame(GeneticTree gTree) throws Exception {
        Board board = Board.simpleBoard();
        Player p = new GeneticAlgorithmPlayer(board,gTree);
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
        ArrayList<Board> plays = new ArrayList<>(trees.size());
        for(GeneticTree g : trees) {
            Board b = Board.simpleBoard();
            GeneticAlgorithmPlayer ga = new GeneticAlgorithmPlayer(b, g);
            ga.spawn(b.getPlayerSpawn());
            b.registerActor(ga);
            plays.add(b);
            b.play();
        }
        plays.sort((x, y) -> y.getPlayer().getScore() - x.getPlayer().getScore());
        ArrayList<GeneticTree> ts = new ArrayList<GeneticTree>(trees.size());
        for(Board b : plays) {
            GeneticAlgorithmPlayer gpp = (GeneticAlgorithmPlayer)b.getPlayer();
            ts.add(gpp.getDecisionTree());
        }
        return ts;
    }

    public static List<GeneticTree> mate(List<GeneticTree> stuff) {
        List<GeneticTree> elite = new ArrayList<GeneticTree>(stuff.size());
        for(int i = 0; i < stuff.size(); ++i) {
            int rand = Board.rng.nextInt(stuff.size()-i) + i;
            elite.add(new GeneticTree(stuff.get(i), stuff.get(rand)));
        }
        return elite;
    }

    public static void main(String[] args) throws Exception {
        int count = 100;
        List<GeneticTree> l = firstPop(count);
        for(int i = 0; i < 5; ++i) {
            l = run(l);
            mate(l);
        }
        drawGame(l.get(0));
    }
}
