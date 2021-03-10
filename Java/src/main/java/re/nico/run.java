package re.nico;

import re.nico.async.*;
import re.nico.count.*;

public class run {
    public static void main(String[] args)
    {

        BlockingFutureDemo.run();
        ContinuationsDemo.run();
        CompletableFutureDemo.run();
        ListenableFutureDemo.run();
        AsyncHandlersDemo.run();
        ReactiveStreamsDemo.run();
        System.out.println("=============================");
        System.out.println();

        RunCounter.run(new SynchronizedMethodsCounter(), 0, 23, 8);
        RunCounter.run(new SynchronizedThisCounter(), 0, 23, 8);
        RunCounter.run(new SynchronizedObjectCounter(), 0, 23, 8);
        RunCounter.run(new AtomicCounter(), 0, 23, 8);
        RunCounter.run(new TransactionalCounterMultiverse(), 0, 23, 8);
        RunCounter.run(new TransactionalCounterScala(), 0, 23, 8);
        RunCounter.run(new TransactionalCounterNarayana(), 0, 23, 8);
        RunCounter.run(new UnsafeCounter(), 0, 23, 8);

        System.out.println();
        System.out.println("#########");
        System.out.println("# Done! #");
        System.out.println("#########");
        System.out.println();
        return;
    }
}
