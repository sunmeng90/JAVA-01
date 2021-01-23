import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Clients {
    public static void main(String[] args) throws IOException {
        send_to_socket_server(null, 9001);
        send_to_socket_server(null, 9002);
        send_to_socket_server(null, 9003);
    }

    public static void send_to_socket_server(String host, int port) throws IOException {
        Socket socket = new Socket(Optional.ofNullable(host).orElse("localhost"), port);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello from client".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
