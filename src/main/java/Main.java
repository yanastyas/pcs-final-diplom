import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    private static final int PORT = 8989;
    private static BooleanSearchEngine engine;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        System.out.println(engine.search("бизнес"));
        try (ServerSocket serverSocket = new ServerSocket(8989)) { // старт сервера
            while (true) { // в цикле принимаем подключения
                try (
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    String word = in.readLine();

                    List<PageEntry> list = engine.search(word);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setPrettyPrinting();
                    Gson gson = gsonBuilder.create();

                    out.println(gson.toJson(list));

                    break;
                } catch (IOException e) {
                    System.out.println("Не могу запустить сервер!");
                    e.printStackTrace();
                }
            }
        }
    }
}
