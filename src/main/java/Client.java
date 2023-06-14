import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;

public class Client {
    static String request = "ЭТАП";

    public static void main(String[] args) throws IOException {

        var inetAddress = Inet4Address.getByName("localhost");

        try (var socket = new Socket(inetAddress, 8989);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(request);
            String answer;
            while ((answer = in.readLine()) != null) {
                System.out.println("Результат с сервера: " + answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}