package re.nico.async;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import java.io.IOException;
import static org.asynchttpclient.Dsl.asyncHttpClient;

public class CompletableFutureBranchless {
 
  public static void run()  {
    try (AsyncHttpClient asyncHttpClient = asyncHttpClient()) {
      asyncHttpClient
              .prepareGet("http://www.nicobosshard.ch/Hi.html")
              .execute()
              .toCompletableFuture()
              .thenApply(Response::getResponseBody)
              .thenAccept(System.out::println)
              .join();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Done!");
  }
}
