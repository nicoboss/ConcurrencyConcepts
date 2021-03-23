package re.nico.async;

import static org.asynchttpclient.Dsl.*;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpUtils;

import io.netty.handler.codec.http.HttpHeaders;

public class AsyncHandlers {

    public static String run() throws InterruptedException, ExecutionException {
        Future<String> asyncHandler = asyncHttpClient().prepareGet("http://www.nicobosshard.ch/Hi.html")
                .execute(new AsyncHandler<String>() {
                    private Charset charset = StandardCharsets.UTF_8;
                    private StringBuilder htmlSource = new StringBuilder();

                    @Override
                    public State onStatusReceived(HttpResponseStatus statusCode) throws Exception {
                        System.out.println("[AsyncHandlers] onStatusReceived: " + Thread.currentThread().getName());
                        return (statusCode.getStatusCode() == 200) ? State.CONTINUE : State.ABORT;
                    }

                    @Override
                    public State onHeadersReceived(HttpHeaders headers) throws Exception {
                        System.out.println("[AsyncHandlers] onHeadersReceived: " + Thread.currentThread().getName());
                        Charset specifiedCharset = HttpUtils
                                .extractContentTypeCharsetAttribute(headers.get("Content-Type"));
                        if (specifiedCharset != null)
                            charset = specifiedCharset;
                        return State.CONTINUE;
                    }

                    @Override
                    public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                        System.out.println("[AsyncHandlers] onBodyPartReceived: " + Thread.currentThread().getName());
                        htmlSource.append(new String(bodyPart.getBodyPartBytes(), charset));
                        return State.CONTINUE;
                    }

                    @Override
                    public String onCompleted() throws Exception {
                        System.out.println("[AsyncHandlers] onCompleted: " + Thread.currentThread().getName());
                        return htmlSource.toString();
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        System.out.println("[AsyncHandlers] onThrowable: " + Thread.currentThread().getName());
                        t.printStackTrace();
                    }
                });

        System.out.println("[ListenableFutureDemo] Main vor Task: " + Thread.currentThread().getName());
        BigInteger.probablePrime(256, new Random()); // Task während Request
        System.out.println("[ListenableFutureDemo] Main nach Task: " + Thread.currentThread().getName());
        return asyncHandler.get();
    }
}
