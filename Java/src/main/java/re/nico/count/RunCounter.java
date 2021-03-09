package re.nico.count;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class RunCounter {

    private static AtomicBoolean counterCheck = new AtomicBoolean(false);

    public static long speedTest(Counter counter, int counts, int tester) {
        final ExecutorService executor = Executors.newCachedThreadPool();
        final List<Future<Integer>> futures = new ArrayList<>();
        long timer = System.currentTimeMillis();
        for (int i = 0; i < tester; ++i) {
            futures.add(executor.submit(new CountTask(counter, counts)));
        }
        Iterator<Future<Integer>> it = futures.iterator();
        while (it.hasNext()) {
            try {
                if (it.next().get() == 0) {
                    counterCheck.set(true);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                return -1;
            }
        }
        if (counter.get() != 0) {
            System.out.println("Error: " + counter.get() + " != 0");
            counterCheck.set(false);
        }
        long dauer = System.currentTimeMillis() - timer;
        executor.shutdown();
        return dauer;
    }

    public static void run(Counter counter, long summe, int durchlaeufe, int tester) {
        summe = 0;
        for (int i = 0; i < durchlaeufe; ++i) {
            summe += speedTest(counter, 100000, tester);
        }
        System.out.println(counter.getClass().getName() + ": Durchschnittszeit = " + summe / durchlaeufe + " ms");
        if (counterCheck.get()) {
            System.out.println("Test Erfolgreich!");
        } else {
            System.out.println("Test Fehlgeschlagen!");
        }
    }
}
