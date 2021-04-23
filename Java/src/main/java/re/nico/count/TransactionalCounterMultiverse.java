package re.nico.count;

import org.multiverse.api.references.*;
import static org.multiverse.api.StmUtils.*;

public final class TransactionalCounterMultiverse implements Counter {
    private final TxnInteger i;
    public TransactionalCounterMultiverse() {
        i = newTxnInteger(0);
    }
    @Override public void increment() {
        atomic(() -> {
            i.increment();
        });
    }
    @Override public void decrement() {
        atomic(() -> {
            i.decrement();
        });
    }
    @Override public int get() {
        return atomic(() -> {
            return i.get();
        });
    }
}
