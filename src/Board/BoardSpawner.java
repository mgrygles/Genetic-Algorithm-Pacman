package Board;

import Actor.Actor;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ahanes on 2/21/15.
 */
public class BoardSpawner {
    private List<ActorRespawn> actors;

    public BoardSpawner() {
        this.actors = new LinkedList<ActorRespawn>();
    }

    public void add(Actor a, int ticks) {
        this.actors.add(new ActorRespawn(a, ticks));
    }

    public List<Actor> tick() {
        List<Actor> a = new LinkedList<Actor>();
        for (ActorRespawn ar : this.actors) {
            ar.ticks -= 1;
            if (ar.ticks <= 0) {
                a.add(ar.a);
            }
        }
        this.actors.removeAll(a);
        return a;
    }

    protected class ActorRespawn {
        public Actor a;
        public int ticks;

        public ActorRespawn(Actor a, int ticks) {
            this.a = a;
            this.ticks = ticks;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ActorRespawn)) return false;

            ActorRespawn that = (ActorRespawn) o;

            if (ticks != that.ticks) return false;
            if (a != null ? !a.equals(that.a) : that.a != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = a != null ? a.hashCode() : 0;
            result = 31 * result + ticks;
            return result;
        }
    }
}
