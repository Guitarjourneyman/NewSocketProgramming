import java.io.*;
import java.net.Socket;

public class TcpSocketConnection {
    private static final int PORT = 1995;
    private Socket socket;
    private SenderViewModel sender; // SenderViewModel �ν��Ͻ�

    public void startClient(String serverIP) {
        try {
            socket = new Socket(serverIP, PORT);
            
            System.out.println("Client: " + serverIP + " is connected by TCP" + " & index: " + NewSocket.clients_tcp_index);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TCP ���� �޽����� �����ϴ� �޼���
    public void sendEchoMessage(String message) {
        if (sender != null) {
            sender.sendMessage_tcp(message); // SenderViewModel�� ����Ͽ� �޽��� ����
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
