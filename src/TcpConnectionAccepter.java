import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

public class TcpConnectionAccepter {
    private static final int PORT = 8189; // 수신할 포트
    private ServerSocket serverSocket;
    private Server_Tcp receiver_tcp; // Server_Tcp 인스턴스
    //private JTextArea receivedMessagesArea;  // GUI의 receive message 창
    public void startServer(JTextArea receivedMessagesArea) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");
            
            Socket clientSocket = serverSocket.accept(); // 클라이언트 연결 수락
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                
            receiver_tcp = new Server_Tcp(clientSocket,receivedMessagesArea);  
         // 수신 스레드 시작
            startReceiverThread();
            System.out.println("TCP Server Listening started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 // 수신을 위한 스레드
    private void startReceiverThread() {
        new Thread(() -> {
            try {
                System.out.println("Listening message by tcp ~ ");
                receiver_tcp.startReceiving(); // ReceiverViewModel의 수신 메서드 호출
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    
}
