package re.nico.async;

import static org.asynchttpclient.Dsl.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.asynchttpclient.*;
import org.asynchttpclient.util.HttpUtils;

import io.netty.handler.codec.http.HttpHeaders;

public class AsyncHandlers {

    public static void run() {
        Future<Integer> asyncHandler = asyncHttpClient().prepareGet("http://www.nicobosshard.ch/Hi.html")
                .execute(new AsyncHandler<Integer>() {
                    private Integer status;
                    private Charset charset = StandardCharsets.UTF_8;

                    @Override
                    public State onStatusReceived(HttpResponseStatus statusCode) throws Exception {
                        status = statusCode.getStatusCode();
                        if (status != 200) {
                            return State.ABORT;
                        }
                        return State.CONTINUE;
                    }

                    @Override
                    public State onHeadersReceived(HttpHeaders headers) throws Exception {
                        Charset specifiedCharset = HttpUtils.extractContentTypeCharsetAttribute(headers.get("Content-Type"));
                        if (specifiedCharset != null) charset = specifiedCharset;
                        return State.CONTINUE;
                    }

                    @Override
                    public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                        System.out.println(new String(bodyPart.getBodyPartBytes(), charset));
                        return State.CONTINUE;
                    }

                    @Override
                    public Integer onCompleted() throws Exception {
                        return status;
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        t.printStackTrace();
                    }
                });

        try {
            if (asyncHandler.get() == 200) {
                System.out.println("Done!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
