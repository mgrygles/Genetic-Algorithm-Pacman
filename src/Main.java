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

    public static void main(String[] args) throws Exception {
        int count = 1000;
        List<GeneticTree> l = firstPop(count);
        l = run(l);
        drawGame(l.get(0));
    }
}
