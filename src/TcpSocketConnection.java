import java.io.*;
import java.net.Socket;

public class TcpSocketConnection {
    private static final int PORT = 1995;
    private Socket socket;
    private SenderViewModel sender; // SenderViewModel �ν��Ͻ�
    private ReceiverViewModel receiver; // ReceiverViewModel �ν��Ͻ�
    public int permanent_id; // ���� �޽��� �迭 �ε����� �����ϱ� ���� ���� ��ȣ 
    
    public void startClient(String serverIP) {
        try {
            socket = new Socket(serverIP, PORT);
            sender = new SenderViewModel(socket);
            receiver = new ReceiverViewModel(socket); // ReceiverViewModel �ν��Ͻ� ����
            permanent_id = NewSocket.clients_tcp_index;
            System.out.println("Client: " + serverIP + " is connected by TCP" + " & index: " + NewSocket.clients_tcp_index);
            if(NewSocket.clients_tcp_index == 0)
			{	
				NewSocket.clients_tcp.set(NewSocket.clients_tcp_index, false);  // �ʱ� �ε����� false�� �ʱ�
				permanent_id = NewSocket.clients_tcp_index;
				System.out.println("connected by TCP"+ " & index: " + NewSocket.clients_tcp_index);
				NewSocket.clients_tcp_index++;
			}
			else { //index�� 0�� �ƴϸ� �迭�� �ø� 
				NewSocket.clients_tcp.add(NewSocket.clients_tcp_index, false);  // ���� �ε����� false�� �ʱ�
				permanent_id = NewSocket.clients_tcp_index;
				System.out.println("connected by TCP"+ " & index: " + NewSocket.clients_tcp_index);
				NewSocket.clients_tcp_index++; // index�� = client�� + 1 
				
				
			}
            // ���� ������ ����
            startReceiverThread();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ReceiverViewModel receiverViewModel_tcp() {
    	return receiver;
    }
    // ������ ���� ������
    private void startReceiverThread() {
        new Thread(() -> {
            try {
                System.out.println("Listening message by tcp ~ ");
                receiver.startReceiving(); // ReceiverViewModel�� ���� �޼��� ȣ��
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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
