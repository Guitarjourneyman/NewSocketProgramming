import java.io.*;
import java.net.Socket;

public class TcpSocketConnection {
    private static final int PORT = 8189;
    private Socket socket;
    private Client_Tcp client; // SenderViewModel �ν��Ͻ�
    private Server_Tcp server; // ReceiverViewModel �ν��Ͻ�

    
    public void startClient(String serverIP) {
        try {
            socket = new Socket(serverIP, PORT);
            client = new Client_Tcp(socket);
            server = new Server_Tcp(socket); // Server_Tcp �ν��Ͻ� ����
            
            System.out.println("Client: " + serverIP + " is connected by TCP" + " & index: " + NewSocket.clients_tcp_index);
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Server_Tcp receiverViewModel_tcp() {
    	return server;
    }
    // ������ ���� ������
    private void startReceiverThread() {
        new Thread(() -> {
            try {
                System.out.println("Listening message by tcp ~ ");
                server.startReceiving(); // ReceiverViewModel�� ���� �޼��� ȣ��
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // TCP ���� �޽����� �����ϴ� �޼���
    public void sendEchoMessage(String message) {
        if (client != null) {
            client.sendMessage_tcp(message); // SenderViewModel�� ����Ͽ� �޽��� ����
        } else {
            System.out.println("SenderViewModel�� �ʱ�ȭ���� �ʾҽ��ϴ�.");
        }
    }

    // ���� ���� �޼��� �߰�
    public void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("TCP ������ �������ϴ�.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
