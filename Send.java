import java.io.*;
import java.net.*;

public class Send {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: send [host] [port] [text]");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String text = args[2];

        try (Socket socket = new Socket(host, port)) {
            // Create output stream to send data to the server
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);

            // Send the text to the server
            writer.println(text);

            System.out.println("Message sent to server: " + text);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
