package board.actors.geneticplayer;

import board.Board;
import board.tiles.BoardNode;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by ahanes on 3/14/15.
 */
public class GeneticTree {
    private List<Predicate<GeneticAlgorithmPlayer>> predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private List<MoveLambda> terminals = new ArrayList<MoveLambda>();
    private List<Predicate<GeneticAlgorithmPlayer>> predicates_used = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private List<MoveLambda> terminals_used = new ArrayList<MoveLambda>();
    private List<String> predicate_strings = new ArrayList<String>();
    private List<String> terminal_strings = new ArrayList<String>();
    private MoveTree mTree;

    interface MoveLambda {
        BoardNode move(GeneticAlgorithmPlayer p);
    }

    public GeneticTree() {
        HashSet<Object> used = new HashSet<Object>();
        predicates.add((p) -> (p.nearestGhost(p.getLocation()) > p.border));
        predicates.add((p) -> (p.nearestEnergizer(p.getLocation()) > p.border));
        predicates.add((p) -> (p.hasEnergizer(p.getLocation())));
        predicates.add((p) -> (p.inDanger()));
        predicates.add((p) -> (p.invulnerable()));
        predicates.add((p) -> (p.nearestEnergizerIsSafe(p.getLocation(), p.border)));
        predicate_strings.add("Nearest Ghost");
        predicate_strings.add("Nearest Energizer");
        predicate_strings.add("Has Energizer");
        predicate_strings.add("In Danger");
        predicate_strings.add("Invulnerable");
        predicate_strings.add("Nearest Energizer is safe");

        terminals.add((p) -> (p.rand_move()));
        terminals.add((p) -> (p.safestMove()));
        terminals.add((p) -> (p.attackMove()));
        terminals.add((p) -> (p.chain_move()));
        terminals.add((p) -> (p.energizer_move()));
        terminal_strings.add("Rand");
        terminal_strings.add("Safest");
        terminal_strings.add("Attack");
        terminal_strings.add("Chain");
        terminal_strings.add("Energizer");
        int rand = Board.rng.nextInt(terminals.size());
        this.mTree = this.buildMoveTree();
        System.out.println(this.mTree);
    }

    private MoveTree buildMoveTree() {
        if(this.predicates.isEmpty()) {
            int index = Board.rng.nextInt(terminals.size());
            return new pred_term(terminals.get(index), terminal_strings.get(index));
        }
        else {
            int index = Board.rng.nextInt(predicates.size());
            Predicate<GeneticAlgorithmPlayer> p = this.predicates.remove(index);
            String s = predicate_strings.remove(index);
            return new pred(p, buildMoveTree(), buildMoveTree(), s);
        }
    }

    public BoardNode move(GeneticAlgorithmPlayer p) {
        return this.mTree.eval(p);
    }

    public GeneticTree(GeneticTree a, GeneticTree b) {
        Random rng = Board.rng;
    }

    interface MoveTree {
        BoardNode eval(GeneticAlgorithmPlayer player);
    }

    private class pred implements MoveTree {
        Predicate<GeneticAlgorithmPlayer> t;
        private String n;
        MoveTree l, r;
        public pred(Predicate<GeneticAlgorithmPlayer> t, MoveTree l, MoveTree r, String n) {
            this.t = t;
            this.n = n;
            this.l = l;
            this.r = r;
        }

        @Override
        public BoardNode eval(GeneticAlgorithmPlayer player) {
            return this.t.test(player) ? r.eval(player) : l.eval(player);
        }

        public String toString() {
            return String.format("( %s %s %s )", this.n, this.l.toString(), this.r.toString());
        }
    }

    private class pred_term implements MoveTree {
        MoveLambda m;
        String n;
        public pred_term(MoveLambda m, String n) {
            this.m = m;
            this.n = n;
        }

        public BoardNode eval(GeneticAlgorithmPlayer player) {
            System.out.println("Called " + this.n);
            return this.m.move(player);
        }

        public String toString() {
            return String.format("( %s )", this.n);
        }


    }
}
