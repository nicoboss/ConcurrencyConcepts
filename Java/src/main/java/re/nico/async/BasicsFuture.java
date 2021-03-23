package re.nico.async;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.asynchttpclient.Response;
import static org.asynchttpclient.Dsl.*;

public class BasicsFuture {

    public static String run() throws InterruptedException, ExecutionException {
        Future<Response> blockingFuture = asyncHttpClient().prepareGet("http://www.nicobosshard.ch/Hi.html").execute();
        System.out.println("[BasicsFuture] Main vor Task: " + Thread.currentThread().getName());
        BigInteger.probablePrime(256, new Random()); // Task während Request
        System.out.println("[BasicsFuture] Main nach Task: " + Thread.currentThread().getName());
        return blockingFuture.get().getResponseBody();
    }
}
