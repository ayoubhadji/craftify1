package utils;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MiniServer extends NanoHTTPD {

    private static MiniServer instance;
    private static int port;

    private MiniServer(int assignedPort) throws IOException {
        super(assignedPort);
        start(SOCKET_READ_TIMEOUT, false);
        System.out.println("Serveur embarqué lancé sur http://localhost:" + assignedPort);
    }

    public static MiniServer getInstance() {
        return instance;
    }

    public static int getPort() {
        return port;
    }

    public static void startServer() {
        if (instance == null) {
            try {
                port = findFreePort();
                instance = new MiniServer(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort(); // OS te donne un port libre
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            String html = Files.readString(Paths.get("src/main/resources/org.example/user/reCaptcha/recaptcha.html"));
            return newFixedLengthResponse(Response.Status.OK, "text/html", html);
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Erreur de chargement HTML");
        }
    }
}
