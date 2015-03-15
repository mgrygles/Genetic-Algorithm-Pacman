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
        predicate_strings.add("Is In Danger");
        predicate_strings.add("Is Invulnerable");
        predicate_strings.add("Nearest Energizer is safe");

        //terminals.add((p) -> (p.rand_move()));
        terminals.add((p) -> (p.safestMove()));
        terminals.add((p) -> (p.attackMove()));
        terminals.add((p) -> (p.chain_move()));
        terminals.add((p) -> (p.energizer_move()));
        //terminal_strings.add("Rand");
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
            return new MoveTreeLeaf(terminals.get(index), terminal_strings.get(index));
        }
        else {
            int index = Board.rng.nextInt(predicates.size());
            Predicate<GeneticAlgorithmPlayer> p = this.predicates.remove(index);
            String s = predicate_strings.remove(index);
            return new MoveTreePredicate(p, buildMoveTree(), buildMoveTree(), s);
        }
    }

    public BoardNode move(GeneticAlgorithmPlayer p) {
        return this.mTree.eval(p);
    }

    public GeneticTree(GeneticTree a, GeneticTree b) {
        this.mTree = merge(a.mTree, b.mTree);
    }

    private MoveTree merge(MoveTree a, MoveTree b) {
        MoveTree n;
        if(Board.rng.nextBoolean()) {
            n = copy(a);
        }
        else {
            n = copy(b);
        }
        if(n instanceof MoveTreePredicate) {
            MoveTreePredicate p = (MoveTreePredicate) n;
            MoveTreePredicate p1 = (MoveTreePredicate) a;
            MoveTreePredicate p2 = (MoveTreePredicate) b;
            p.l = merge(p1.l, p2.l);
            p.r = merge(p1.r, p2.r);
        }
        return n;
    }

    private MoveTree copy(MoveTree a) {
        if(a instanceof MoveTreePredicate) {
            MoveTreePredicate a2 = (MoveTreePredicate)a;
            return new MoveTreePredicate(a2.t, copy(a2.l), copy(a2.r), a2.n);
        }
        else { //Terminal
            MoveTreeLeaf a2 = (MoveTreeLeaf) a;
            return new MoveTreeLeaf(a2.m, a2.n);
        }
    }

    interface MoveTree {
        BoardNode eval(GeneticAlgorithmPlayer player);
    }

    private class MoveTreePredicate implements MoveTree {
        Predicate<GeneticAlgorithmPlayer> t;
        String n;
        MoveTree l, r;
        public MoveTreePredicate(Predicate<GeneticAlgorithmPlayer> t, MoveTree l, MoveTree r, String n) {
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

    private class MoveTreeLeaf implements MoveTree {
        MoveLambda m;
        String n;
        public MoveTreeLeaf(MoveLambda m, String n) {
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
