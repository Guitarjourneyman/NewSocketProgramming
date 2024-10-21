import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

public class TcpConnectionAccepter {
    private static final int PORT = 8189; // ������ ��Ʈ
    private ServerSocket serverSocket;
    

    public void startServer(JTextArea receivedMessagesArea) {
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");

            // Ŭ���̾�Ʈ ������ ����ϸ鼭, �� ���ῡ ���� ���ο� �����带 ����
            while (NewSocket.clients_tcp_index == 0) {
                Socket clientSocket = serverSocket.accept(); // Ŭ���̾�Ʈ ���� ����
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // �� Ŭ���̾�Ʈ�� ���� ���ο� �ڵ鷯 �����带 ����
                ClientHandler clientHandler = new ClientHandler(clientSocket, receivedMessagesArea);
                new Thread(clientHandler).start();
            }
            
            System.out.println("Stop Clients join Requests");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // �� Ŭ���̾�Ʈ���� ����� ó���ϴ� Ŭ����
    public class ClientHandler implements Runnable {
        
        private final Server_Tcp receiverTcp;
        public final int permanentId;
        

        public ClientHandler(Socket clientSocket, JTextArea receivedMessagesArea) {
            
        	this.permanentId = NewSocket.clients_tcp_index;

            // �ε����� �����ϰ�, ����� Ŭ���̾�Ʈ�� ó���� �� �ֵ��� ����
            synchronized (NewSocket.clients_tcp) {
                if (NewSocket.clients_tcp_index == 0) {
                    NewSocket.clients_tcp.set(NewSocket.clients_tcp_index, false);
                    NewSocket.clients_tcp_index++;
                } else {
                    NewSocket.clients_tcp.add(NewSocket.clients_tcp_index, false);
                    
                }
                System.out.println("Client: " + clientSocket.getInetAddress() + " is connected by TCP"
                        + " & index: " + NewSocket.clients_tcp_index);
                
            }

            this.receiverTcp = new Server_Tcp(clientSocket, receivedMessagesArea);
            System.out.println("Made ClientHandler Successfully");
        }

        @Override
        public void run() {
            try {
            	 // TCP Echo �޽����� �����Ͽ����� üũ�ϴ� ������ ����
                StartTCPCheckThread tcpCheckThread = new StartTCPCheckThread(receiverTcp, this );
                new Thread(tcpCheckThread).start();
                System.out.println("TCP Echo Check Thread gets started");

                // ���� ������ ����
                receiverTcp.startReceiving();
                System.out.println("TCP Echo receive gets started");
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
