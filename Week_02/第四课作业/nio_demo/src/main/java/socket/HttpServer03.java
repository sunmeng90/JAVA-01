package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * one thread do accept and do business logic in reused thread in threadpool
 */
public class HttpServer03 {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket ss = new ServerSocket(9003);
        EchoHttpHandler handler = new EchoHttpHandler();

        while (true) {
            Socket socket = ss.accept();
            executorService.submit(() -> {
                try {
                    handler.handle(socket);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
