package re.nico.count;

import org.junit.jupiter.api.Test;

final class CounterTest {

    @Test
    void testSynchronizedMethodsCounter() {
        RunCounter.run(new SynchronizedMethodsCounter(), 23, 8);
    }

    @Test
    void testSynchronizedThisCounter() {
        RunCounter.run(new SynchronizedThisCounter(), 23, 8);
    }

    @Test
    void testSynchronizedObjectCounter() {
        RunCounter.run(new SynchronizedObjectCounter(), 23, 8);
    }

    @Test
    void testAtomicCounter() {
        RunCounter.run(new AtomicCounter(), 23, 8);
    }

    @Test
    void testTransactionalCounterMultiverse() {
        RunCounter.run(new TransactionalCounterMultiverse(), 23, 8);
    }

    @Test
    void testTransactionalCounterScala() {
        RunCounter.run(new TransactionalCounterScala(), 23, 8);
    }

    @Test
    void testTransactionalCounterNarayana() {
        RunCounter.run(new TransactionalCounterNarayana(), 23, 8);
    }

    @Test
    void testUnsafeCounter() {
        RunCounter.run(new UnsafeCounter(), 23, 8);
    }
}
