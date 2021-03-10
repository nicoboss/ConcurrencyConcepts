package re.nico;

import re.nico.async.*;

public class run {
    public static void main(String[] args)
    {

        BlockingFutureDemo.run();
        ContinuationsDemo.run();
        CompletableFutureDemo.run();
        ListenableFutureDemo.run();
        AsyncHandlersDemo.run();
        ReactiveStreamsDemo.run();
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
