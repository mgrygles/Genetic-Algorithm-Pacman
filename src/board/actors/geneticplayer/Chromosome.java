package board.actors.geneticplayer;

import board.Board;
import board.tiles.BoardNode;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by ahanes on 3/14/15.
 */
public class Chromosome {
    private static List<Predicate<GeneticAlgorithmPlayer>> all_predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private static List<MoveLambda> all_terminals = new LinkedList<MoveLambda>();
    public double score = 0;
    private List<Predicate<GeneticAlgorithmPlayer>> predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private List<MoveLambda> terminals = new LinkedList<MoveLambda>();
    private HashMap<Predicate<GeneticAlgorithmPlayer>, String> predicateToStr = new HashMap<>();
    private HashMap<MoveLambda, String> actionToStr = new HashMap<>();
    private MoveTree mTree;
    private int border_ghost = Board.rng.nextInt(5);
    private int border_energizer = Board.rng.nextInt(5);
    private int border_safeenergizer = Board.rng.nextInt(15);

    public Chromosome() {
        HashSet<Object> used = new HashSet<Object>();
        predicates.add((p) -> (p.nearestGhost(p.getLocation()) > border_ghost));
        predicates.add((p) -> (p.nearestEnergizer(p.getLocation()) > border_energizer));
        predicates.add((p) -> (p.inDanger()));
        predicates.add((p) -> (p.invulnerable()));
        predicates.add((p) -> (p.nearestEnergizerIsSafe(p.getLocation(), border_safeenergizer)));
        predicateToStr.put(predicates.get(0), "Nearest Ghost");
        predicateToStr.put(predicates.get(1), "Nearest Energizer");
        predicateToStr.put(predicates.get(2), "In Danger");
        predicateToStr.put(predicates.get(3), "Invulnerable");
        predicateToStr.put(predicates.get(4), "Nearest Energizer Safe");
        /*
        List<Predicate<GeneticAlgorithmPlayer>> not_predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
        for(Predicate<GeneticAlgorithmPlayer> p : predicates) {
            Predicate<GeneticAlgorithmPlayer> p2 = p.negate();
            not_predicates.add(p2);
            predicateToStr.put(p2, "Not " + predicateToStr.get(p));
        }
        predicates.addAll(not_predicates);*/

        //terminals.add((p) -> (p.rand_move()));
        terminals.add((p) -> (p.safestMove()));
        terminals.add((p) -> (p.attackMove()));
        terminals.add((p) -> (p.chain_move()));
        terminals.add((p) -> (p.energizer_move()));
        //actionToStr.put(terminals.get(0), "rand");
        actionToStr.put(terminals.get(0), "safest");
        actionToStr.put(terminals.get(1), "attack");
        actionToStr.put(terminals.get(2), "chain");
        actionToStr.put(terminals.get(3), "energizer");
        if (Chromosome.all_terminals.size() == 0) {
            Chromosome.all_terminals.addAll(this.terminals);
            Chromosome.all_predicates.addAll(this.predicates);
        }
        Collections.shuffle(terminals, Board.rng);
        this.mTree = this.buildMoveTree(4);
    }

    public Chromosome(Chromosome a, Chromosome b) {
        this.border_energizer = (Board.rng.nextBoolean() ? a.border_energizer : b.border_energizer);
        this.border_ghost = (Board.rng.nextBoolean() ? a.border_ghost: b.border_ghost);
        this.border_safeenergizer= (Board.rng.nextBoolean() ? a.border_safeenergizer: b.border_safeenergizer);
        this.mTree = merge(a.mTree, b.mTree);
    }

    private MoveTree buildMoveTree(int size) {
        if (size == 0) {
            int index = Board.rng.nextInt(terminals.size());
            return new MoveTreeLeaf(terminals.get(index), this.actionToStr.get(terminals.get(index)));
        } else {
            int index = Board.rng.nextInt(predicates.size());
            Predicate<GeneticAlgorithmPlayer> p = this.predicates.get(index);
            String s = this.predicateToStr.get(p);
            return new MoveTreePredicate(p, buildMoveTree(size - 1), buildMoveTree(size - 1), s);
        }
    }

    public BoardNode move(GeneticAlgorithmPlayer p) {
        return this.mTree.eval(p);
    }

    public void mutate() {
        if (Board.rng.nextFloat() > .65) {
            Chromosome g = new Chromosome(this, new Chromosome());
            this.mTree = g.mTree;
        }
        if(Board.rng.nextBoolean()) {
            if(Board.rng.nextBoolean()) {
                this.border_energizer = this.border_energizer + Board.rng.nextInt((this.border_energizer/2)+1);
                this.border_ghost= this.border_ghost+ Board.rng.nextInt((this.border_ghost/2)+1);
                this.border_safeenergizer= this.border_safeenergizer+ Board.rng.nextInt((this.border_safeenergizer/2)+1);
            } else {
                this.border_energizer = this.border_energizer - Board.rng.nextInt((this.border_energizer/2)+1);
                this.border_ghost= this.border_ghost - Board.rng.nextInt((this.border_ghost/2)+1);
                this.border_safeenergizer= this.border_safeenergizer - Board.rng.nextInt((this.border_safeenergizer/2)+1);
            }
        }
    }

    private MoveTree merge(MoveTree a, MoveTree b) {
        MoveTree n;
        if (Board.rng.nextBoolean()) {
            n = copy(a);
        } else {
            n = copy(b);
        }
        MoveTreePredicate foo = ((MoveTreePredicate) n);
        MoveTreePredicate foo1 = ((MoveTreePredicate) a);
        MoveTreePredicate foo2 = ((MoveTreePredicate) b);
        if (Board.rng.nextBoolean()) {
            foo.l = copy(foo1.l);
            foo.r = copy(foo2.r);
        } else {
            foo.r = copy(foo1.r);
            foo.l = copy(foo1.l);
        }
        return n;
    }


    public String toString() {
        return "Borders: \n\tSafe Energizer: " +this.border_safeenergizer + "\n\tGhost:" + this.border_ghost +
                "\n\tEnergizer:" + this.border_energizer + "\n" + this.mTree.toString();
    }

    private MoveTree copy(MoveTree a) {
        if (a instanceof MoveTreePredicate) {
            MoveTreePredicate a2 = (MoveTreePredicate) a;
            return new MoveTreePredicate(a2.t, copy(a2.l), copy(a2.r), a2.n);
        } else { //Terminal
            MoveTreeLeaf a2 = (MoveTreeLeaf) a;
            return new MoveTreeLeaf(a2.m, a2.n);
        }
    }

    interface MoveLambda {
        BoardNode move(GeneticAlgorithmPlayer p);
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
            if (Board.Debug)
                System.out.println("Called " + this.n);
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
            if (Board.Debug)
                System.out.println("Called " + this.n + "\n");
            return this.m.move(player);
        }

        public String toString() {
            return String.format("( %s )", this.n);
        }


    }
}
