import java.io.*;
import java.net.Socket;


public class TcpSocketConnection {
	private static final int PORT = 1995;
	public int perminent_id; //에코메시지 배열 인덱스와 연결하기 위한 고유 번호 
    private Socket socket;
	private SenderViewModel sender; // SenderViewModel 인스턴스
	
	public void startClient(String serverIP) {

		
		
		
		try {
			socket = new Socket(serverIP, PORT);
			// SenderViewModel 인스턴스 생성
            sender = new SenderViewModel(socket);
            
			if(NewSocket.clients_tcp_index == 0)
			{	
				NewSocket.clients_tcp.set(NewSocket.clients_tcp_index, false);  // 초기 인덱스는 false로 초기
				perminent_id = NewSocket.clients_tcp_index;
				System.out.println("Client: " + serverIP + "is connected by TCP"+ " & index: " + NewSocket.clients_tcp_index);
				NewSocket.clients_tcp_index++;
			}
			else {
				NewSocket.clients_tcp.add(NewSocket.clients_tcp_index, false);  // 다음 인덱스는 false로 초기
				perminent_id = NewSocket.clients_tcp_index;
				System.out.println("Client: " + serverIP + "is connected by TCP"+ " & index: " + NewSocket.clients_tcp_index);
				NewSocket.clients_tcp_index++; // index값 = client수 + 1 
				
				
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
	
	// TCP 에코 메시지를 전송하는 메서드
    public void sendEchoMessage(String message) {
        if (sender != null) {
            sender.sendMessage_tcp(message); // SenderViewModel을 사용하여 메시지 전송
        } else {
            System.out.println("SenderViewModel이 초기화되지 않았습니다.");
        }
    }
}
