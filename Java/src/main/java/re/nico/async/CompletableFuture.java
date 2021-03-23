package re.nico.async;

import org.asynchttpclient.AsyncHttpClient;
import java.math.BigInteger;
import java.util.Random;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class CompletableFuture {

  public static String run() {
    AsyncHttpClient asyncHttpClient = asyncHttpClient();
    var completableFuture = asyncHttpClient.prepareGet("http://www.nicobosshard.ch/Hi.html").execute()
        .toCompletableFuture().whenComplete((result, ex) -> {
          System.out.println("[CompletableFuture] Fertig");
        });
    System.out.println("[CompletableFuture] Main vor Task: " + Thread.currentThread().getName());
    BigInteger.probablePrime(256, new Random()); // Task während Request
    System.out.println("[CompletableFuture] Main nach Task: " + Thread.currentThread().getName());
    return completableFuture.join().getResponseBody();
  }
}
