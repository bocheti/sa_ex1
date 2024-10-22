import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Program {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("  socket send [host] [port] [text]");
            System.out.println("  socket recv [port]");
            System.out.println("  socket sendf [host] [port] [file_path]");
            System.out.println("  socket recvf [port]");
            return;
        }

        String command = args[0];
        try {
            switch (command) {
                case "send":
                    if (args.length == 4) {
                        String host = args[1];
                        int port = Integer.parseInt(args[2]);
                        String message = args[3];
                        sendText(host, port, message);
                    } else {
                        System.out.println("Usage: socket send [host] [port] [text]");
                    }
                    break;
                case "recv":
                    if (args.length == 2) {
                        int port = Integer.parseInt(args[1]);
                        recvText(port);
                    } else {
                        System.out.println("Usage: socket recv [port]");
                    }
                    break;
                case "sendf":
                    if (args.length == 4) {
                        String host = args[1];
                        int port = Integer.parseInt(args[2]);
                        String filePath = args[3];
                        sendFile(host, port, filePath);
                    } else {
                        System.out.println("Usage: socket sendf [host] [port] [file_path]");
                    }
                    break;
                case "recvf":
                    if (args.length == 2) {
                        int port = Integer.parseInt(args[1]);
                        recvFile(port);
                    } else {
                        System.out.println("Usage: socket recvf [port]");
                    }
                    break;
                default:
                    System.out.println("Invalid command. Use 'send', 'recv', 'sendf', or 'recvf'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to send a text message
    private static void sendText(String host, int port, String message) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            writer.println(message);
            System.out.println("Message sent: " + message);
        }
    }

    // Method to receive a text message
    private static void recvText(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Client connected.");
            String message = reader.readLine();
            System.out.println("Received message: " + message);
        }
    }

    // Method to send a file
    private static void sendFile(String host, int port, String filePath) throws IOException {
        try (Socket socket = new Socket(host, port);
             FileInputStream fileInputStream = new FileInputStream(filePath);
             DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {

            File file = new File(filePath);

            // Send the file name first
            dataOut.writeUTF(file.getName());

            // Send the file contents
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }

            dataOut.flush();
            System.out.println("File sent: " + file.getName());
        }
    }

    // Method to receive a file
    private static void recvFile(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept();
             DataInputStream dataIn = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected.");

            // Read the file name sent by the sender
            String fileName = dataIn.readUTF();
            String newFileName = generateUniqueFileName(fileName);

            // Save the file
            try (FileOutputStream fileOutputStream = new FileOutputStream(newFileName)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = dataIn.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("File received: " + newFileName);
            }
        }
    }

    // Method to generate a unique file name at the receiver's end
    private static String generateUniqueFileName(String originalFileName) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return originalFileName + "_" + timeStamp;
        } else {
            String namePart = originalFileName.substring(0, dotIndex);
            String extensionPart = originalFileName.substring(dotIndex);
            return namePart + "_" + timeStamp + extensionPart;
        }
    }
}
