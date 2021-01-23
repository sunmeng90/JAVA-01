package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * One thread do accept and business logic
 */
public class HttpServer01 {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket ss = new ServerSocket(9001);
        EchoHttpHandler handler = new EchoHttpHandler();

        while (true) {
            Socket socket = ss.accept();
            handler.handle(socket);
        }
    }

}
