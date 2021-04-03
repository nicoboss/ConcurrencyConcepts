package re.nico.async;

import java.net.URL;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class SingleThreadExecutorDemo {

    public static void run() {
        ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
        for (int i = 0; i < 10; ++i) {
            completionService.submit(() -> {
                System.out.println("[SingleThreadScheduledExecutorDemo] Request von " + Thread.currentThread().getName());
                return new BufferedReader(new InputStreamReader(new URL("http://www.nicobosshard.ch/Hi.html").openStream())).lines().collect(Collectors.joining("\n"));
            });
        }
        System.out.println("[SingleThreadScheduledExecutorDemo] Main vor Task: " + Thread.currentThread().getName());
        BigInteger.probablePrime(256, new Random()); // Task während Request
        System.out.println("[SingleThreadScheduledExecutorDemo] Main nach Task: " + Thread.currentThread().getName());
        for (int i = 0; i < 10; ++i) {
            try {
                System.out.println(completionService.take().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
