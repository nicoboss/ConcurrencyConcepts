package re.nico.prng;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReactivePRNG {

public void run() throws InterruptedException {
	Flowable.fromCallable(() -> {
		// Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
		var seed = 1;
		long ulong1 = Long.parseUnsignedLong("11400714819323198485");
		long ulong2 = Long.parseUnsignedLong("13787848793156543929");
		long ulong3 = Long.parseUnsignedLong("10723151780598845931");
		for (int i=0; i < 100; ++i) {
			seed += ulong1;
			long z = seed;
			z = (z ^ (z >> 30)) * ulong2;
			z = (z ^ (z >> 27)) * ulong3;
			System.out.println("Generated!");
			System.out.println((z ^ (z >> 31)) >> 31);
		}
		return "Done!";
	}).subscribeOn(Schedulers.io()).observeOn(Schedulers.single()).subscribe(System.out::println,
			Throwable::printStackTrace);
	System.out.println("Done!!!");
}

}
