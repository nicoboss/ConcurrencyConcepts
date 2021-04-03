package re.nico.async;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class SingleThreadScheduledExecutorDemo {

    static BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();

    public static void run() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        final ScheduledFuture<?> promise = executor.scheduleAtFixedRate(() -> {
            System.out.println("[SingleThreadScheduledExecutorDemo] Request um " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + " von " + Thread.currentThread().getName());
            try {
                blockingQueue.add(new BufferedReader(new InputStreamReader(new URL("http://www.nicobosshard.ch/Hi.html").openStream())).lines().collect(Collectors.joining("\n")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
        executor.schedule(() -> promise.cancel(false), 1, TimeUnit.MINUTES);
        for (int i = 0; i < 10; ++i) {
            System.out.println("[SingleThreadScheduledExecutorDemo] Main vor Task: " + Thread.currentThread().getName());
            BigInteger.probablePrime(256, new Random()); // Task während Request
            System.out.println("[SingleThreadScheduledExecutorDemo] Main nach Task: " + Thread.currentThread().getName());
            System.out.println(blockingQueue.take());
        }
        System.out.println("[SingleThreadScheduledExecutorDemo] Done!");
    }
}
