package re.nico.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.asynchttpclient.Response;
import static org.asynchttpclient.Dsl.*;

public class BlockingFuture {

    public static void run() {
        try {
            Future<Response> blockingFuture = asyncHttpClient().prepareGet("http://www.nicobosshard.ch/Hi.html").execute();
            Response response = blockingFuture.get();
            System.out.println(response.getResponseBody());
            if (response.getStatusCode() == 200) {
                System.out.println("Done!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
