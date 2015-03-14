package board.actors.geneticplayer;

import board.Board;
import board.tiles.BoardNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by ahanes on 3/14/15.
 */
public class GeneticTree {
    private List<Predicate<GeneticAlgorithmPlayer>> predicates = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private List<MoveLambda> terminals = new ArrayList<MoveLambda>();
    private List<Predicate<GeneticAlgorithmPlayer>> predicates_used = new ArrayList<Predicate<GeneticAlgorithmPlayer>>();
    private List<MoveLambda> terminals_used = new ArrayList<MoveLambda>();
    private MoveTree mTree;

    interface MoveLambda {
        BoardNode move(GeneticAlgorithmPlayer p);
    }

    public GeneticTree() {
        predicates.add((p) -> (p.nearestGhost(p.getLocation()) > p.border));
        predicates.add((p) -> (p.nearestEnergizer(p.getLocation()) > p.border));
        predicates.add((p) -> (p.hasEnergizer(p.getLocation())));
        predicates.add((p) -> (p.inDanger()));
        predicates.add((p) -> (p.invulnerable()));
        predicates.add((p) -> (p.nearestEnergizerIsSafe(p.getLocation(), p.border)));

        terminals.add((p) -> (p.rand_move()));
        terminals.add((p) -> (p.safestMove()));
        terminals.add((p) -> (p.attackMove()));
        terminals.add((p) -> (p.chain_move()));
        terminals.add((p) -> (p.energizer_move()));

        Collections.shuffle(predicates, Board.rng);
        Collections.shuffle(terminals, Board.rng);
        this.mTree = new pred_term(terminals.get(0));
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
        MoveTree l, r;
        public pred(Predicate<GeneticAlgorithmPlayer> t, MoveTree l, MoveTree r) {
            this.t = t;
        }

        @Override
        public BoardNode eval(GeneticAlgorithmPlayer player) {
            return this.t.test(player) ? r.eval(player) : l.eval(player);
        }
    }

    private class pred_term implements MoveTree {
        MoveLambda m;
        public pred_term(MoveLambda m) {
            this.m = m;
        }

        public BoardNode eval(GeneticAlgorithmPlayer player) {
            return this.m.move(player);
        }


    }
}
