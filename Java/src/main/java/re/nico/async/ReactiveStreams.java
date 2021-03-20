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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class ReactiveStreams {

    public static void run() {
        try {
            ListenableFuture<MyHandler> future = asyncHttpClient()
                    .prepareGet("http://www.nicobosshard.ch/Hi.html").execute(new MyHandler());
            byte[] result = future.get().getBytes();
            System.out.println(new String(result, "UTF-8"));
            System.out.println("Done!");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static protected class MyHandler implements StreamedAsyncHandler<MyHandler> {
        private final MySubscriber<HttpResponseBodyPart> sub;

        MyHandler() {
            this(new MySubscriber<>());
        }

        MyHandler(MySubscriber<HttpResponseBodyPart> sub) {
            this.sub = sub;
        }

        @Override
        public State onStream(Publisher<HttpResponseBodyPart> pub) {
            pub.subscribe(sub);
            return State.CONTINUE;
        }

        @Override
        public void onThrowable(Throwable t) {
            throw new AssertionError(t);
        }

        @Override
        public State onBodyPartReceived(HttpResponseBodyPart part) {
            throw new AssertionError("Should not have received body part");
        }

        @Override
        public State onStatusReceived(HttpResponseStatus statusCode) {
            return State.CONTINUE;
        }

        @Override
        public State onHeadersReceived(HttpHeaders headers) {
            return State.CONTINUE;
        }

        @Override
        public MyHandler onCompleted() {
            return this;
        }

        public byte[] getBytes() throws Throwable {
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

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }

        @Override
        public void onNext(T t) {
            elements.add(t);
            subscription.request(1);
        }

        @Override
        public void onError(Throwable error) {
            this.error = error;
            latch.countDown();
        }

        @Override
        public void onComplete() {
            latch.countDown();
        }

        public List<T> getElements() throws Throwable {
            latch.await();
            if (error != null) {
                throw error;
            } else {
                return elements;
            }
        }
    }
}
