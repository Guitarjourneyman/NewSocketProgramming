import java.io.*;
import java.net.Socket;


public class TcpSocketConnection {
	private static final int PORT = 1995;
	public int perminent_id; //���ڸ޽��� �迭 �ε����� �����ϱ� ���� ���� ��ȣ 
	
	public void startClient(String serverIP) {

		Socket socket = null;
		
		
		try {
			socket = new Socket(serverIP, PORT);
			
			if(NewSocket.clients_tcp_index == 0)
			{	
				NewSocket.clients_tcp.set(NewSocket.clients_tcp_index, false);  // �ʱ� �ε����� false�� �ʱ�
				perminent_id = NewSocket.clients_tcp_index;
				System.out.println("Client: " + serverIP + "is connected by TCP"+ " & index: " + NewSocket.clients_tcp_index);
				NewSocket.clients_tcp_index++;
			}
			else {
				NewSocket.clients_tcp.add(NewSocket.clients_tcp_index, false);  // ���� �ε����� false�� �ʱ�
				perminent_id = NewSocket.clients_tcp_index;
				System.out.println("Client: " + serverIP + "is connected by TCP"+ " & index: " + NewSocket.clients_tcp_index);
				NewSocket.clients_tcp_index++; // index�� = client�� + 1 
				
				
			}
			
	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				if (socket != null)
					socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
