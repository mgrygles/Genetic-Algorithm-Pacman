package ui;

import board.Board;
import board.actors.Actor;
import board.tiles.BoardNode;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by ahanes on 2/19/15.
 */
public class PacmanUI extends JFrame {

    HashMap<CoordPair, JLabel> map;
    //Use https://gist.githubusercontent.com/hankwang/5626452/raw/af9a87b44063f9b0b9e4d2fabda6fc8a502d34c7/Pacman.java
    // for reference
    private GridLayout gbl;
    private Board b;

    public PacmanUI(Board b) {
        this.b = b;
        this.setTitle("Pacman");
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(500, 500));
        this.setLocationRelativeTo(null);
        this.gbl = new GridLayout(b.getRows(), b.getCols());
        map = new HashMap<CoordPair, JLabel>();
        for (BoardNode[] row : b.getBoard()) {
            for (BoardNode node : row) {
                JLabel n = new JLabel(node.toString());
                n.setOpaque(true);
                n.setHorizontalAlignment(SwingConstants.CENTER);
                n.setVerticalAlignment(SwingConstants.CENTER);
                CoordPair c = new CoordPair(node.getX(), node.getY());
                map.put(c, n);
                this.add(n);
            }
        }
        this.setLayout(gbl);
        this.pack();
        Insets insets = this.getInsets();
        this.setSize(new Dimension(insets.left + insets.right + 500,
                insets.top + insets.bottom + 500));
        this.revalidate();
    }

    public void redrawGrid(Board b) {
        HashMap<CoordPair, Actor> actors = new HashMap<CoordPair, Actor>();
        for (Actor a : b.getActors()) {
            actors.put(new CoordPair(a.getLocation().getX(), a.getLocation().getY()), a);
        }
        for (BoardNode[] row : b.getBoard()) {
            for (BoardNode node : row) {
                CoordPair c = new CoordPair(node.getX(), node.getY());
                if (!actors.containsKey(c) || !actors.get(c).isActive()) {
                    map.get(c).setText(node.getBoardCharacter().toString());
                    map.get(c).setBackground(node.getBackgroundColor());
                    map.get(c).setForeground(node.getForegroundColor());
                } else {
                    map.get(c).setText(actors.get(c).getBoardCharacter().toString());
                    map.get(c).setBackground(actors.get(c).getBackgroundColor());
                    map.get(c).setForeground(actors.get(c).getForegroundColor());
                }
            }
        }
        this.revalidate();
        this.repaint();
    }

    protected class CoordPair {
        private int x, y;

        public CoordPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

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
    }
}
