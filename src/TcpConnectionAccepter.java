import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpConnectionAccepter {
    private static final int PORT = 1997; // ������ ��Ʈ
    private ServerSocket serverSocket;

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Ŭ���̾�Ʈ ���� ����
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
