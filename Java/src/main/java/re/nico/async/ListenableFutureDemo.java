package re.nico.async;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;

import static org.asynchttpclient.Dsl.*;

public class ListenableFutureDemo {

    public static String run() throws InterruptedException, ExecutionException {
        ListenableFuture<Response> whenResponse = asyncHttpClient().prepareGet("http://www.nicobosshard.ch/Hi.html")
                .execute();
        Callable<String> callback = () -> {
            System.out.println("[ListenableFutureDemo] Callback vor get(): " + Thread.currentThread().getName());
            Response response = whenResponse.get();
            System.out.println("[ListenableFutureDemo] Callback nach get(): " + Thread.currentThread().getName());
            return response.getResponseBody();
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        var future = executorService.submit(callback);
        whenResponse.addListener(
                () -> System.out.println("[ListenableFutureDemo] Listener: " + Thread.currentThread().getName()),
                executorService);
        System.out.println("[ListenableFutureDemo] Main vor Task: " + Thread.currentThread().getName());
        BigInteger.probablePrime(256, new Random()); // Task während Request
        System.out.println("[ListenableFutureDemo] Main nach Task: " + Thread.currentThread().getName());
        return future.get();
    }

}
