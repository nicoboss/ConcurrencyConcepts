package re.nico.count;

import org.jboss.stm.annotations.Transactional;
import org.jboss.stm.Container;
import org.jboss.stm.annotations.Pessimistic;
import org.jboss.stm.annotations.ReadLock;
import org.jboss.stm.annotations.WriteLock;
import com.arjuna.ats.arjuna.AtomicAction;

public final class TransactionalCounterNarayana implements Counter {

    private Atomic atomicObj;

    public TransactionalCounterNarayana() {
        atomicObj = new Container<Atomic>().create(new NarayanaCounter());
    }

    @Override
    public void increment() {
        AtomicAction atomicAction = new AtomicAction();
        atomicAction.begin();
        atomicObj.increment();
        atomicAction.commit();
    }

    @Override
    public void decrement() {
        AtomicAction atomicAction = new AtomicAction();
        atomicAction.begin();
        atomicObj.decrement();
        atomicAction.commit();
    }

    @Override
    public int get() {
        return atomicObj.get();
    }

    @Transactional @Pessimistic
    public interface Atomic
    {
        public void increment();
        public void decrement();
        public int get();
    }

    public class NarayanaCounter implements Atomic {

        private int state;

        @Override @ReadLock
        public int get() {
            return state;
        }
    
        @Override @ReadLock @WriteLock
        public void increment() {
            ++state;
        }
    
        @Override @ReadLock @WriteLock
        public void decrement() {
            --state;
        }
    }
}
