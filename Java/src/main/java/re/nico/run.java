package re.nico;

import re.nico.async.*;

public class run {
    public static void main(String[] args)
    {

        BlockingFuture.run();
        CompletableFutureBranchless.run();
        CompletableFuture.run();
        ListenableFutureDemo.run();
        CompletableFutureBranchless.run();
        ReactiveStreams.run();
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
