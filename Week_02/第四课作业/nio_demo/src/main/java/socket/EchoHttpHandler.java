package socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoHttpHandler {

    public void handle(Socket socket) throws IOException, InterruptedException {
        System.out.println("got connection, start process");
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Type:text/html;charset=utf-8");
        String body = "hello,nio" + ", get your input: ";
        printWriter.println("Content-Length:" + body.getBytes().length);
        printWriter.println();
        printWriter.write(body);
        printWriter.close();
        socket.close();
        System.out.println("finish processing, write back");
    }
}
