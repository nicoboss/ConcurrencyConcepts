package re.nico;

import java.util.concurrent.ExecutionException;

import re.nico.async.*;

public class run {
    public static void main(String[] args) throws InterruptedException, ExecutionException, Throwable
    {

        System.out.println(BasicsFuture.run());
        System.out.println(AsyncHandlers.run());
        System.out.println(CompletableFuture.run());
        System.out.println(ListenableFutureDemo.run());
        System.out.println(ReactiveStreams.run());
        System.out.println("=============================");
        System.out.println();

        System.out.println();
        System.out.println("#########");
        System.out.println("# Done! #");
        System.out.println("#########");
        System.out.println();
        return;
    }
}
