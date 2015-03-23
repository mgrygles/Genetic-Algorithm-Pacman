package board;

import board.actors.Actor;
import board.actors.Ghost;
import board.actors.Player;
import board.tiles.BoardNode;
import board.tiles.GhostSpawnNode;
import board.tiles.PlayerSpawnNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by ahanes on 2/15/15.
 */
public class Board {
    public static final Random rng = new Random(2);
    public static final long MAX_TURNS = 1000;
    public static boolean Debug = false;
    private BoardSpawner spawnQueue;
    private BoardNode[][] board;
    private BoardNode ghostSpawn;
    private BoardNode playerSpawn;
    private List<Actor> actors;
    private int rows, cols;

    private HashMap<Actor, BoardNode> locations;
    private boolean over = false;

    public Board(File text) throws FileNotFoundException {
        this.locations = new HashMap<Actor, BoardNode>();
        this.actors = new LinkedList<Actor>();
        this.board = this.boardListToArray(this.readBoard(text));
        this.rows = board.length;
        this.cols = board[0].length;
        this.spawnQueue = new BoardSpawner();
    }

    public static Board simpleBoard() throws Exception {
        Board b = new Board(new File("board.txt"));
        List<Actor> actors = new ArrayList<Actor>();
        for (int i2 = 0; i2 < 4; ++i2) {
            Actor a = new Ghost(b);
            actors.add(a);
            b.registerActor(a);
            a.spawn(b.getGhostSpawn());
        }
        return b;
    }

    public BoardSpawner getSpawnQueue() {
        return spawnQueue;
    }

    public BoardNode[][] getBoard() {
        return board;
    }

    public void setBoard(BoardNode[][] board) {
        this.board = board;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public long play() {
        long count = 0;
        while (!this.isOver() && this.MAX_TURNS > count) {
            this.boardTick();
            ++count;
        }
        return count;
    }

    public void boardTick() {
        List<Actor> spawns = spawnQueue.tick();
        for (Actor a : spawns) {
            if (a.isActive()) a.spawn();
        }
        for (BoardNode[] b : this.board) {
            for (BoardNode a : b) {
                a.deactivate();
            }
        }
        for (Actor a : this.actors) {
            if (a.isActive()) {
                BoardNode last = a.getLocation();
                if (!last.getNeighbors(a).contains(a.move())) {
                    System.err.println("board.actors made invalid move!");
                    System.exit(1);
                }
            }
        }
        for (Actor a : this.actors) {
            if (a instanceof Player) {
                ((Player) a).tick();
            }
            for (Actor b : this.actors) {
                if (a.getLocation() == b.getLocation() && !a.equals(b)) {
                    a.collision(b);
                }
            }
        }
    }

    public void registerActor(Actor a) {
        this.locations.put(a, a.getLocation());
        this.actors.add(a);
    }

    public void deregisterActor(Actor a) {
        this.actors.remove(a);
        this.locations.remove(a);
    }

    protected ArrayList<ArrayList<BoardNode>> readBoard(File text) throws FileNotFoundException {
        HashMap<Pair, BoardNode> map = new HashMap<>();
        ArrayList<ArrayList<BoardNode>> board = new ArrayList<ArrayList<BoardNode>>();
        Scanner br = new Scanner(new FileReader(text));
        int x = 0;
        while (br.hasNextLine()) {
            String l = br.nextLine().trim();
            int y = 0;
            ArrayList<BoardNode> row = new ArrayList<BoardNode>();
            for (Character c : l.toCharArray()) {
                BoardNode n = BoardNodeFactory.makeBoardNode(map, c, x, y++);
                if (n instanceof GhostSpawnNode) {
                    this.ghostSpawn = n;
                }

                if (n instanceof PlayerSpawnNode) {
                    this.playerSpawn = n;
                }

                row.add(n);
            }
            ++x;
            board.add(row);
        }
        return board;
    }

    protected BoardNode[][] boardListToArray(ArrayList<ArrayList<BoardNode>> b) {
        BoardNode[][] board = new BoardNode[b.size()][b.get(0).size()];
        int x = 0;
        for (ArrayList<BoardNode> a : b) {
            int y = 0;
            for (BoardNode c : a) {
                board[x][y++] = c;
            }
            ++x;
        }
        return board;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (BoardNode[] a : this.board) {
            for (BoardNode b : a) {
                boolean add = false;
                for (Actor act : this.actors) {
                    if (act.getLocation().equals(b)) {
                        str.append(act.toString());
                        add = true;
                        break;
                    }
                }
                if (!add) {
                    str.append(b.toString());
                }
            }
            str.append('\n');
        }
        return str.toString();
    }

    public Actor getPlayer() {
        for (Actor a : this.actors) {
            if (a instanceof Player) {
                return a;
            }
        }
        return null;
    }

    public BoardNode getPlayerSpawn() {
        return playerSpawn;
    }

    public BoardNode getGhostSpawn() {
        return ghostSpawn;
    }

    public boolean isOver() {
        return over;
    }

    public void gameOver() {
        this.over = true;
    }
}
