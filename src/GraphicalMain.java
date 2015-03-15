import board.Board;
import board.actors.Player;
import board.actors.geneticplayer.GeneticAlgorithmPlayer;
import ui.PacmanUI;

/**
 * Created by ahanes on 2/21/15.
 */
public class GraphicalMain {
    public static void main(String[] args) throws Exception {
        Board board = Board.simpleBoard();
        Player p = new GeneticAlgorithmPlayer(board);
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
}
