//Lose basierend auf github.com/davidgeorgehope/akka-http-java-client
package re.nico.async;

import akka.Done;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.HostConnectionPool;
import akka.http.javadsl.Http;
import akka.http.javadsl.OutgoingConnection;
import akka.http.javadsl.model.*;
import akka.japi.Pair;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import scala.util.Try;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class AkkaHTTP {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("client");
        AkkaHTTPDemo client = new AkkaHTTPDemo(actorSystem);

        client.connectionLevel(Optional.of("Hi"), success ->
                success.entity()
                        .getDataBytes()
                        .runForeach(byteString -> System.out.println(byteString.utf8String()),
                                client.getSystem())
        ).whenComplete((success, throwable) -> client.close());
    }

    public static class AkkaHTTPDemo {

        private final ActorSystem system;
        private final Flow<HttpRequest, HttpResponse, CompletionStage<OutgoingConnection>> connectionFlow;
        private final Flow<Pair<HttpRequest, Integer>, Pair<Try<HttpResponse>, Integer>, HostConnectionPool> poolClientFlow;

        public AkkaHTTPDemo(ActorSystem sys) {
            system = sys;
            connectionFlow = Http.get(system).outgoingConnection(ConnectHttp.toHost("http://www.nicobosshard.ch/", 80));
            poolClientFlow = Http.get(system).cachedHostConnectionPool(ConnectHttp.toHost("http://www.nicobosshard.ch/", 80));
        }

        public ActorSystem getSystem() {
            return system;
        }

        public void close() {
            System.out.println("Shutting down client");
            Http.get(system).shutdownAllConnectionPools().whenComplete((s, f) -> system.terminate());
        }

        private Uri getUri(Optional<String> s) {
            return Uri.create("http://www.nicobosshard.ch/Hi.html");
        }

        public <U> CompletionStage<Done> connectionLevel(Optional<String> s,
                Function<HttpResponse, CompletionStage<Done>> responseHandler) {
            return Source.single(HttpRequest.create().withUri(getUri(s))).via(connectionFlow)
                    .runWith(Sink.head(), system).thenComposeAsync(responseHandler);
        }

        public <U> CompletionStage<U> hostLevel(Optional<String> s,
                Function<Pair<Try<HttpResponse>, Integer>, CompletionStage<U>> responseHandler) {
            return Source.single(Pair.create(HttpRequest.create().withUri(getUri(s)), 42)).via(poolClientFlow)
                    .runWith(Sink.head(), system).thenComposeAsync(responseHandler);
        }

        public <U> CompletionStage<U> requestLevelFutureBased(Optional<String> s,
                Function<HttpResponse, CompletionStage<U>> responseHandler) {
            return Http.get(system).singleRequest(HttpRequest.create().withUri(getUri(s)))
                    .thenComposeAsync(responseHandler);
        }

    }
}
