import java.io.*;
import java.net.Socket;

public class TcpSocketConnection {
    private static final int PORT = 8189;
    private Socket socket;
    private Client_Tcp client; // SenderViewModel 인스턴스
    private Server_Tcp server; // ReceiverViewModel 인스턴스

    
    public void startClient(String serverIP) {
        try {
            socket = new Socket(serverIP, PORT);
            client = new Client_Tcp(socket);
            server = new Server_Tcp(socket); // Server_Tcp 인스턴스 생성
            
            System.out.println("Client: " + serverIP + " is connected by TCP" + " & index: " + NewSocket.clients_tcp_index);
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Server_Tcp receiverViewModel_tcp() {
    	return server;
    }
    // 수신을 위한 스레드
    private void startReceiverThread() {
        new Thread(() -> {
            try {
                System.out.println("Listening message by tcp ~ ");
                server.startReceiving(); // ReceiverViewModel의 수신 메서드 호출
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // TCP 에코 메시지를 전송하는 메서드
    public void sendEchoMessage(String message) {
        if (client != null) {
            client.sendMessage_tcp(message); // SenderViewModel을 사용하여 메시지 전송
        } else {
            System.out.println("SenderViewModel이 초기화되지 않았습니다.");
        }
    }

    // 소켓 종료 메서드 추가
    public void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("TCP 소켓이 닫혔습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
