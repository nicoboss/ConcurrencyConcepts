//Lose basierend auf AkkaQuickstart Beispiel
package re.nico.prng;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class AkkaPRNG {
    public static void main(String[] args) {
        final ActorSystem<MyRandom.printRandomNumbers> actorSystem = ActorSystem.create(MyRandom.create(), "AkkaGenerator");
        actorSystem.tell(new MyRandom.printRandomNumbers(0, 100));
    }

    public static class MyRandom extends AbstractBehavior<MyRandom.printRandomNumbers> {
        public static class printRandomNumbers {
            public final long seed;
            public final int randomNumbersToGenerate;
            public printRandomNumbers(long seed, int randomNumbersToGenerate) {
                this.seed = seed;
                this.randomNumbersToGenerate = randomNumbersToGenerate;
            }
        }
        private final ActorRef<Generator.Generate> prng;
        public static Behavior<printRandomNumbers> create() {
            return Behaviors.setup(MyRandom::new);
        }
        private MyRandom(ActorContext<printRandomNumbers> context) {
            super(context);
            prng = context.spawn(Generator.create(), "generator");
        }
        @Override public Receive<printRandomNumbers> createReceive() {
            return newReceiveBuilder().onMessage(printRandomNumbers.class, this::onPrintRandomNumbers).build();
        }
        private Behavior<printRandomNumbers> onPrintRandomNumbers(printRandomNumbers command) {
            ActorRef<Receiver.RandomNumber> replyTo = getContext().spawn(Receiver.create(), "receiver");
            prng.tell(new Generator.Generate(command.seed, command.randomNumbersToGenerate, replyTo));
            return Behaviors.stopped();
        }
    }

    public static class Receiver extends AbstractBehavior<Receiver.RandomNumber> {
        public static final class RandomNumber {
            public final long randomNumber;
            public RandomNumber(long randomNumber) {
                this.randomNumber = randomNumber;
            }
        }
        public static Behavior<RandomNumber> create() {
            return Behaviors.setup(Receiver::new);
        }
        private Receiver(ActorContext<RandomNumber> context) {
            super(context);
        }
        @Override public Receive<RandomNumber> createReceive() {
            return newReceiveBuilder().onMessage(RandomNumber.class, this::onGenerate).build();
        }
        private Behavior<RandomNumber> onGenerate(RandomNumber command) {
            getContext().getLog().info("Received {}", command.randomNumber);
            return this;
        }
    }

    public static class Generator extends AbstractBehavior<Generator.Generate> {
        public static final class Generate {
            public final long seed;
            public final int randomNumbersToGenerate;
            public final ActorRef<Receiver.RandomNumber> replyTo;
            public Generate(long seed, int randomNumbersToGenerate, ActorRef<Receiver.RandomNumber> replyTo) {
                this.seed = seed;
                this.randomNumbersToGenerate = randomNumbersToGenerate;
                this.replyTo = replyTo;
            }
        }
        public static Behavior<Generate> create() {
            return Behaviors.setup(Generator::new);
        }
        private Generator(ActorContext<Generate> context) {
            super(context);
        }
        @Override public Receive<Generate> createReceive() {
            return newReceiveBuilder().onMessage(Generate.class, this::onGenerate).build();
        }
        private Behavior<Generate> onGenerate(Generate command) {
            // Basieren auf C++ splitmix Generator von Arvid Gerstmann.
            long seed = command.seed;
            long ulong1 = Long.parseUnsignedLong("11400714819323198485");
            long ulong2 = Long.parseUnsignedLong("13787848793156543929");
            long ulong3 = Long.parseUnsignedLong("10723151780598845931");
            for (int i = 0; i < command.randomNumbersToGenerate; ++i) {
                seed += ulong1;
                long z = seed;
                z = (z ^ (z >> 30)) * ulong2;
                z = (z ^ (z >> 27)) * ulong3;
                long randomNumber = (z ^ (z >> 31)) >> 31;
                getContext().getLog().info("Generated {}!", randomNumber);
                command.replyTo.tell(new Receiver.RandomNumber(randomNumber));
            }
            return Behaviors.stopped();
        }
    }
}
