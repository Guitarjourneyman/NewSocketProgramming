import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

public class TcpConnectionAccepter implements Runnable {
    private static final int PORT = 8189; // ������ ��Ʈ
    private ServerSocket serverSocket;
    
    private JTextArea receivedMessagesArea;
    private JTextArea consoleArea;

    public TcpConnectionAccepter(JTextArea receivedMessagesArea, JTextArea consoleArea) {
        this.receivedMessagesArea = receivedMessagesArea;
        this.consoleArea = consoleArea;
    }
    

    public void run() {
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");

            // Ŭ���̾�Ʈ ������ ����ϸ鼭, �� ���ῡ ���� ���ο� �����带 ����
            // Ŭ���̾�Ʈ �ִ� ���� ����
            
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Ŭ���̾�Ʈ ���� ����
                
                /*UDP broad�� Server�� IP �����ϴ� ��Ŀ���� �߰�*/
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // �� Ŭ���̾�Ʈ�� ���� ���ο� �ڵ鷯 �����带 ����
                ClientHandler clientHandler = new ClientHandler(clientSocket, this, receivedMessagesArea);
                new Thread(clientHandler).start();
            }
            //consoleArea.append("TCP ���� ����Ϸ� \n");
            //System.out.println("All TCP Sockets are connnected");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // �� Ŭ���̾�Ʈ���� ����� ó���ϴ� Ŭ����
    public class ClientHandler implements Runnable {
        
        private final Server_Tcp receiverTcp;
        public int permanent_id;
        

        public ClientHandler(Socket clientSocket, TcpConnectionAccepter tcpAccepter, JTextArea receivedMessagesArea) {
            
            
            

            // �ε����� �����ϰ�, ����� Ŭ���̾�Ʈ�� ó���� �� �ֵ��� ����
            synchronized (NewSocket.clients_tcp) {
                if (NewSocket.clients_tcp_index == 0) {
                	permanent_id = NewSocket.clients_tcp_index;
                	
                    NewSocket.clients_tcp.set(NewSocket.clients_tcp_index, false);
                    NewSocket.clients_tcp_index++;
                } else {
                	permanent_id = NewSocket.clients_tcp_index;
                	
                    NewSocket.clients_tcp.add(NewSocket.clients_tcp_index, false);
                    NewSocket.clients_tcp_index++;
                }
                System.out.println("Client: " + clientSocket.getInetAddress() + " is connected by TCP"
                        + " & index: " + NewSocket.clients_tcp_index);
                
            }

            this.receiverTcp = new Server_Tcp(clientSocket, receivedMessagesArea);
        }

        @Override
        public void run() {
            try {
            	// TCP Echo �޽����� �����Ͽ����� üũ�ϴ� ������ ����
                StartTCPCheckThread tcpCheckThread = new StartTCPCheckThread(receiverTcp, this);
                new Thread(tcpCheckThread).start();
                
                // ���� ������ ����
                receiverTcp.startReceiving();

                

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
