package board.actors.geneticplayer;

import board.Board;
import board.tiles.BoardNode;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by ahanes on 3/14/15.
 */
public class Chromosome {
    private List<Predicate<GeneticAlgorithmPlayer>> predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private List<MoveLambda> terminals = new LinkedList<MoveLambda>();


    private static List<Predicate<GeneticAlgorithmPlayer>> all_predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private static List<MoveLambda> all_terminals = new LinkedList<MoveLambda>();

    private static final HashMap<Predicate<GeneticAlgorithmPlayer>, String> predicateToStr = new HashMap<>();
    private static final HashMap<MoveLambda, String> actionToStr = new HashMap<>();
    public double score = 0;
    private int border = Board.rng.nextInt(35);

    public BoardNode move(GeneticAlgorithmPlayer player) {
        int i = 0;
        for(Predicate<GeneticAlgorithmPlayer> p : this.predicates) {
            if(p.test(player)) {
                if(Board.Debug)
                    System.out.println(Chromosome.predicateToStr.get(p));
                if(Board.Debug)
                    System.out.println(this.actionToStr.get(this.terminals.get(i)));
                return this.terminals.get(i).move(player);
            }
            ++i;
        }
        return this.terminals.get(this.predicates.size()).move(player);
    }

    interface MoveLambda {
        BoardNode move(GeneticAlgorithmPlayer p);
    }

    public Chromosome() {
        HashSet<Object> used = new HashSet<Object>();
        predicates.add((p) -> (p.nearestGhost(p.getLocation()) > this.border));
        predicates.add((p) -> (p.nearestEnergizer(p.getLocation()) > this.border));
        predicates.add((p) -> (p.hasEnergizer(p.getLocation())));
        predicates.add((p) -> (p.inDanger()));
        predicates.add((p) -> (p.invulnerable()));
        predicates.add((p) -> (p.nearestEnergizerIsSafe(p.getLocation(), this.border)));
        predicateToStr.put(predicates.get(0), "Nearest Ghost");
        predicateToStr.put(predicates.get(1), "Nearest Energizer");
        predicateToStr.put(predicates.get(2), "Has Energizer");
        predicateToStr.put(predicates.get(3), "In Danger");
        predicateToStr.put(predicates.get(4), "Invulnerable");
        predicateToStr.put(predicates.get(5), "Nearest Energizer Safe");

        List<Predicate<GeneticAlgorithmPlayer>> not_predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
        /*
        for(Predicate<GeneticAlgorithmPlayer> p : predicates) {
            Predicate<GeneticAlgorithmPlayer> p2 = p.negate();
            not_predicates.add(p2);
            predicateToStr.put(p2, "Not " + predicateToStr.get(p));
        }
        predicates.addAll(not_predicates);
        */


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

        if(Chromosome.all_predicates.size() == 0) {
            Chromosome.all_predicates.addAll(this.predicates);
            Chromosome.all_terminals.addAll(this.terminals);
        }

        Collections.shuffle(terminals, Board.rng);
        Collections.shuffle(predicates, Board.rng);
        List<MoveLambda> terminals = new LinkedList<MoveLambda>();
        while(terminals.size() != predicates.size() + 1) {
            terminals.add(this.terminals.get(Board.rng.nextInt(this.terminals.size())));
        }
        this.terminals = terminals;
    }

    public Chromosome(Chromosome a, Chromosome b) {
        List<MoveLambda> t1 = new LinkedList<MoveLambda>();
        List<MoveLambda> t2 = new LinkedList<MoveLambda>();
        List<Predicate<GeneticAlgorithmPlayer>> p1 = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
        List<Predicate<GeneticAlgorithmPlayer>> p2 = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
        if(Board.rng.nextBoolean()) {
            //Take from first
            this.border = a.border;
            t1 = a.terminals.subList(0, a.terminals.size()/2);
            t2 = b.terminals.subList(a.terminals.size()/2, a.terminals.size());
            this.terminals.addAll(t1);
            this.terminals.addAll(t2);

            this.predicates.addAll(a.predicates.subList(0, a.predicates.size()/2));
            this.predicates.addAll(b.predicates.subList(b.predicates.size() / 2, b.predicates.size()));
        }
        else {
            //Take from second
            this.border = b.border;
            t2 = a.terminals.subList(0, a.terminals.size()/2);
            t1 = b.terminals.subList(a.terminals.size()/2, a.terminals.size());
            this.terminals.addAll(t1);
            this.terminals.addAll(t2);
            this.predicates.addAll(b.predicates.subList(0, b.predicates.size() / 2));
            this.predicates.addAll(b.predicates.subList(a.predicates.size() / 2, a.predicates.size()));
        }
    }

    public void mutate() {
        if(Board.rng.nextFloat() > .50) {
            // Two choices
            if(Board.rng.nextBoolean()) {
                int a, b;
                a = Board.rng.nextInt(this.predicates.size());
                b = Board.rng.nextInt(this.predicates.size());
                Predicate<GeneticAlgorithmPlayer> p = this.predicates.get(a);
                this.predicates.set(a, this.predicates.get(b));
                this.predicates.set(b, p);
            }
            else {
                if(Board.rng.nextBoolean()) {
                    // Mutate predicate
                    this.predicates.set(Board.rng.nextInt(this.predicates.size()), Chromosome.all_predicates.get(Board.rng.nextInt(Chromosome.all_predicates.size())));
                } else {
                    //Mutate terminal
                    this.terminals.set(Board.rng.nextInt(this.terminals.size()), Chromosome.all_terminals.get(Board.rng.nextInt(Chromosome.all_terminals.size())));
                }
            }
        }
        if(Board.rng.nextFloat() > .5) {
            if(Board.rng.nextBoolean()) {
                this.border --;
            } else {
                this.border++;
            }
        }
        if(Board.rng.nextFloat() > .9) {
            int index = Board.rng.nextInt(this.predicates.size());
            this.predicates.set(index, this.predicates.get(index).and(this.all_predicates.get(Board.rng.nextInt(this.all_predicates.size()))));
        }
    }

    public String toString() {
        String s = "Border: %s\nPredicates:\n";
        for(Predicate<GeneticAlgorithmPlayer> p : this.predicates) {
            s += "\t"+Chromosome.predicateToStr.get(p) + "\n";
        }
        s += "Terminals:\n";
        for(MoveLambda m : this.terminals) {
            s += "\t"+Chromosome.actionToStr.get(m) + "\n";
        }
        return String.format(s, this.border);
    }
}
