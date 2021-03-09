package re.nico.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import static org.asynchttpclient.Dsl.*;

public class ListenableFutureDemo {

    public static void run() {
        ListenableFuture<Response> whenResponse = asyncHttpClient().prepareGet("http://www.nicobosshard.ch/Hi.html").execute();
        Runnable callback = () -> {
            try  {
                Response response = whenResponse.get();
                System.out.println(response.getResponseBody());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        };
        Executor executor = Executors.newCachedThreadPool();
        executor.execute(callback);
        whenResponse.addListener(() -> System.out.println("Done!"), executor);
    }

}
