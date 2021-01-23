package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * one thread do accept and do business logic in new thread
 */
public class HttpServer02 {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9002);
        EchoHttpHandler handler = new EchoHttpHandler();
        while (true) {
            Socket socket = ss.accept();
            new Thread(() -> {
                try {
                    handler.handle(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }

}
