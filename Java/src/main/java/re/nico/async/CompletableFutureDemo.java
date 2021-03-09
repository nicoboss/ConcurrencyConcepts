package re.nico.async;
import org.asynchttpclient.AsyncHttpClient;
import java.io.IOException;
import static org.asynchttpclient.Dsl.asyncHttpClient;

public class CompletableFutureDemo {
 
  public static void run()  {
    try (AsyncHttpClient asyncHttpClient = asyncHttpClient()) {
      asyncHttpClient
              .prepareGet("http://www.nicobosshard.ch/Hi.html")
              .execute()
              .toCompletableFuture()
              .whenComplete((result, ex) -> {
                System.out.println(result.getResponseBody());
                if (result.getStatusCode() == 200) System.out.println("Done!");
                if (ex != null) ex.printStackTrace();
              })
              .join();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
