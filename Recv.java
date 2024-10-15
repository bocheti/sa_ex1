import java.io.*;
import java.net.*;

public class Recv {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: recv [port]");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            // Accept incoming client connections
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            // Create input stream to receive data from client
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // Read and print the received message
            String receivedMessage = reader.readLine();
            System.out.println("Message received from client: " + receivedMessage);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}