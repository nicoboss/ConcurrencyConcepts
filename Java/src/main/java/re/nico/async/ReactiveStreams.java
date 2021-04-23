package re.nico.async;

import io.netty.handler.codec.http.HttpHeaders;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.handler.StreamedAsyncHandler;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class ReactiveStreams {

    public static String run() throws InterruptedException, ExecutionException, Throwable {
        ListenableFuture<MyHandler> future = asyncHttpClient().prepareGet("http://www.nicobosshard.ch/Hi.html")
            .execute(new MyHandler());
        System.out.println("[CompletableFuture] Main vor Task: " + Thread.currentThread().getName());
        BigInteger.probablePrime(256, new Random()); // Task während Request
        System.out.println("[CompletableFuture] Main nach Task: " + Thread.currentThread().getName());
        byte[] result = future.get().getBytes();
        return new String(result, "UTF-8");
    }

    static protected class MyHandler implements StreamedAsyncHandler<MyHandler> {
        private final MySubscriber<HttpResponseBodyPart> sub;
        MyHandler() {
            this(new MySubscriber<>());
            System.out.println("[ReactiveStreams] [Handler] Constructor: " + Thread.currentThread().getName());
        }
        MyHandler(MySubscriber<HttpResponseBodyPart> sub) {
            System.out.println(
                    "[ReactiveStreams] [Handler] Constructor mit Subscriber: " + Thread.currentThread().getName());
            this.sub = sub;
        }
        @Override public State onStream(Publisher<HttpResponseBodyPart> pub) {
            System.out.println("[ReactiveStreams] [Handler] onStream: " + Thread.currentThread().getName());
            pub.subscribe(sub);
            return State.CONTINUE;
        }
        @Override public void onThrowable(Throwable t) {
            System.out.println("[ReactiveStreams] [Handler] onThrowable: " + Thread.currentThread().getName());
            throw new AssertionError(t);
        }
        @Override public State onBodyPartReceived(HttpResponseBodyPart part) {
            System.out.println("[ReactiveStreams] [Handler] onBodyPartReceived: " + Thread.currentThread().getName());
            throw new AssertionError("Should not have received body part");
        }

        @Override public State onStatusReceived(HttpResponseStatus statusCode) {
            System.out.println("[ReactiveStreams] [Handler] onStatusReceived: " + Thread.currentThread().getName());
            return State.CONTINUE;
        }
        @Override public State onHeadersReceived(HttpHeaders headers) {
            System.out.println("[ReactiveStreams] [Handler] onHeadersReceived: " + Thread.currentThread().getName());
            return State.CONTINUE;
        }
        @Override public MyHandler onCompleted() {
            System.out.println("[ReactiveStreams] [Handler] onCompleted: " + Thread.currentThread().getName());
            return this;
        }
        public byte[] getBytes() throws Throwable {
            System.out.println("[ReactiveStreams] [Handler] getBytes: " + Thread.currentThread().getName());
            List<HttpResponseBodyPart> parts = sub.getElements();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            for (HttpResponseBodyPart item : parts) {
                bytes.write(item.getBodyPartBytes());
            }
            return bytes.toByteArray();
        }
    }

    static protected class MySubscriber<T> implements Subscriber<T> {
        private final List<T> elements = Collections.synchronizedList(new ArrayList<>());
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile Subscription subscription;
        private volatile Throwable error;
        @Override public void onSubscribe(Subscription subscription) {
            System.out.println("[ReactiveStreams] [Subscriber] onSubscribe: " + Thread.currentThread().getName());
            this.subscription = subscription;
            subscription.request(1);
        }
        @Override public void onNext(T t) {
            System.out.println("[ReactiveStreams] [Subscriber] onNext: " + Thread.currentThread().getName());
            elements.add(t);
            subscription.request(1);
        }
        @Override public void onError(Throwable error) {
            System.out.println("[ReactiveStreams] [Subscriber] onError: " + Thread.currentThread().getName());
            this.error = error;
            latch.countDown();
        }
        @Override public void onComplete() {
            System.out.println("[ReactiveStreams] [Subscriber] onComplete: " + Thread.currentThread().getName());
            latch.countDown();
        }
        public List<T> getElements() throws Throwable {
            System.out.println(
                    "[ReactiveStreams] [Subscriber] getElements vor await: " + Thread.currentThread().getName());
            latch.await();
            System.out.println(
                    "[ReactiveStreams] [Subscriber] getElements nach await: " + Thread.currentThread().getName());
            if (error != null) {
                throw error;
            } else {
                return elements;
            }
        }
    }
}
