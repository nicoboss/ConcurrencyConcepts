package re.nico.count;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RunCounter {

    public static long speedTest(Counter counter, int counts, int tester) {
        final ExecutorService executor = Executors.newCachedThreadPool();
        long timer = System.currentTimeMillis();
        for (int i = 0; i < tester; ++i) {
            executor.submit(new CountTask(counter, counts));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(600, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, counter.get());
        long dauer = System.currentTimeMillis() - timer;
        return dauer;
    }

    public static void run(Counter counter, int durchlaeufe, int tester) {
        int dauer = 0;
        for (int i = 0; i < durchlaeufe; ++i) {
            dauer += speedTest(counter, 100000, tester);
        }
        System.out.println(counter.getClass().getName() + ": Durchschnittszeit = " + dauer / durchlaeufe + " ms");
    }
}
