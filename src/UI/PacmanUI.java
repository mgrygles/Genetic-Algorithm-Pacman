package UI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import Board.*;

/**
 * Created by ahanes on 2/19/15.
 */
public class PacmanUI extends JFrame {

    protected class CoordPair {
        private int x, y;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CoordPair)) return false;

            CoordPair coordPair = (CoordPair) o;

            if (x != coordPair.x) return false;
            if (y != coordPair.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        public CoordPair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    //Use https://gist.githubusercontent.com/hankwang/5626452/raw/af9a87b44063f9b0b9e4d2fabda6fc8a502d34c7/Pacman.java
    // for reference
    private GridLayout gbl;
    private Board b;
    HashMap<CoordPair, JLabel> map;
    public PacmanUI(Board b) {
        this.b = b;
        this.setTitle("Pacman");
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(500, 500));
        this.setLocationRelativeTo(null);
        this.gbl = new GridLayout(b.getRows(), b.getCols());
        this.setLayout(gbl);
        map = new HashMap<CoordPair, JLabel>();
        for(BoardNode[] row : b.getBoard()) {
            for(BoardNode node : row) {
                JLabel n = new JLabel(node.toString());
                CoordPair c = new CoordPair(node.getX(), node.getY());
                map.put(c, n);
                this.add(n);
            }
        }
        this.revalidate();

    }

    public void redrawGrid(Board b) {
        for(BoardNode[] row : b.getBoard()) {
            for(BoardNode node : row) {
                CoordPair c = new CoordPair(node.getX(), node.getY());
                map.get(c).setText(node.toString());
            }
        }
        this.revalidate();
        this.repaint();
    }
}
