package re.nico.count;
import static scala.concurrent.stm.japi.STM.*;
import scala.concurrent.stm.Ref;

public final class TransactionalCounterScala implements Counter {
    private Ref.View<Integer> i = newRef(0);
    public TransactionalCounterScala() {
        i = newRef(0);
    }
    @Override public void increment() {
        atomic(() -> {
            i.set(i.get() + 1);
        });
    }
    @Override public void decrement() {
        atomic(() -> {
            i.set(i.get() - 1);
            ;
        });
    }
    @Override public int get() {
        return i.get();
    }
}
