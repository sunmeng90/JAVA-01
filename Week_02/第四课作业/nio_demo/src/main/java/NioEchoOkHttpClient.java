import okhttp3.*;

import java.io.IOException;
import java.time.OffsetDateTime;

public class NioEchoOkHttpClient {

    private final Interceptor HttpLogger = chain -> {
        Request req = chain.request();
        System.out.println("request: " + req.url() + " at " + OffsetDateTime.now());
        Response resp = chain.proceed(req);
        System.out.println("response: " + resp.code() + " at " + OffsetDateTime.now());
        return resp;
    };

    public final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(HttpLogger)
            .build();

    public static void main(String[] args) throws IOException {
        Request req = new Request.Builder()
                .url("http://localhost:8801")
                .get()
                .build();
        Call call = new NioEchoOkHttpClient().client.newCall(req);
        Response response = call.execute();
        System.out.println("Result" + response.body().string());
    }
    
}
