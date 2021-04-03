package re.nico.async;

import java.net.URL;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class FixedThreadPoolDemo {

    public static void run() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
        for (int i = 0; i < 10; ++i) {
            completionService.submit(() -> {
                System.out.println("[FixedThreadPoolDemo] Request von " + Thread.currentThread().getName());
                return new BufferedReader(new InputStreamReader(new URL("http://www.nicobosshard.ch/Hi.html").openStream())).lines().collect(Collectors.joining("\n"));
            });
        }
        System.out.println("[FixedThreadPoolDemo] Main vor Task: " + Thread.currentThread().getName());
        BigInteger.probablePrime(256, new Random()); // Task während Request
        System.out.println("[FixedThreadPoolDemo] Main nach Task: " + Thread.currentThread().getName());
        for (int i = 0; i < 10; ++i) {
            try {
                System.out.println(completionService.take().get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
