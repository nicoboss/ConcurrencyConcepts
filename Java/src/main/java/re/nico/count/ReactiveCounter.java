package re.nico.count;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReactiveCounter {

    //Beispiele basierend auf Erklärungen von https://github.com/ReactiveX/RxJava/README.md
    public static void run() {
        AtomicInteger count = new AtomicInteger();

        Flowable.range(1, 1000).observeOn(Schedulers.computation()).map(v -> count.incrementAndGet())
                .blockingSubscribe(System.out::println);

        Flowable.range(1, 1000)
                .flatMap(v -> Flowable.just(v).subscribeOn(Schedulers.computation()).map(w -> count.incrementAndGet()))
                .blockingSubscribe(System.out::println);

        Observable.range(1, 1000).doOnNext(v -> count.incrementAndGet()).ignoreElements()
                .andThen(Single.fromCallable(() -> count.get())).subscribe(System.out::println);

        Flowable.range(1, 1000).parallel().runOn(Schedulers.computation()).map(v -> count.incrementAndGet())
                .sequential().blockingSubscribe(System.out::println);
        System.out.println("Resultat: " + count.get());
    }

}
