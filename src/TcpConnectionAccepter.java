import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

public class TcpConnectionAccepter {
    private static final int PORT = 8189; // ������ ��Ʈ
    private ServerSocket serverSocket;
    private Server_Tcp receiver_tcp; // Server_Tcp �ν��Ͻ�
    //private JTextArea receivedMessagesArea;  // GUI�� receive message â
    public void startServer(JTextArea receivedMessagesArea) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");
            
            Socket clientSocket = serverSocket.accept(); // Ŭ���̾�Ʈ ���� ����
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                
            receiver_tcp = new Server_Tcp(clientSocket,receivedMessagesArea);  
         // ���� ������ ����
            startReceiverThread();
            System.out.println("TCP Server Listening started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 // ������ ���� ������
    private void startReceiverThread() {
        new Thread(() -> {
            try {
                System.out.println("Listening message by tcp ~ ");
                receiver_tcp.startReceiving(); // ReceiverViewModel�� ���� �޼��� ȣ��
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    
}
