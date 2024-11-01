import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JTextArea;

public class Server_Tcp {
    /* 메시지를 받기만 하는 기능 구현 */
    private Socket socket;
    private static final int PORT = 8189;
    private JTextArea receivedMessagesArea;  // GUI의 receive message 창
    private volatile boolean newEchoReceived_tcp = false; // 에코 메시지 수신 여부
    private int receive_message_num = 0;
    private String lastReceivedMessage = ""; // 최근 수신된 메시지 저장
    
    // 전체 패킷 수와 수신 여부를 추적하는 배열
    private static final int TOTAL_PACKETS = 61; // 전체 패킷 수 (필요에 맞게 수정)
    private boolean[] packetReceived = new boolean[TOTAL_PACKETS + 1]; // 인덱스 0은 사용하지 않음
    private int receivedPacketCount = 0;
    
    // 생성자에서 JTextArea 전달 받음
    public Server_Tcp(Socket socket, JTextArea receivedMessagesArea) {
        this.socket = socket;
        this.receivedMessagesArea = receivedMessagesArea;
    }
    
    public void reset_message_num() {
        receive_message_num = 0;
    }
    
    public boolean hasNewEchoMessage() {
        return newEchoReceived_tcp;
    }

    public void resetNewEchoMessageFlag() {
        newEchoReceived_tcp = false;
    }
    
    // 최근 수신된 메시지 반환 메서드 추가
    public String getLastReceivedMessage() {
        return lastReceivedMessage;
    }

    public void startReceiving() throws IOException { //예외 throw하여 ClientHandler에서 처리하도록함
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            // BufferedReader를 사용하여 데이터를 송수신
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientIP = socket.getInetAddress().getHostAddress();
            System.out.println("Server_TCP is open");

            // 클라이언트와의 연결을 유지하면서 메시지를 지속적으로 수신
            String receivedMessage;
            while (!socket.isClosed() && (receivedMessage = in.readLine()) != null) {
                // 수신된 메시지 처리
            	
            	
                receive_message_num++;
                lastReceivedMessage = receivedMessage; // 최근 수신된 메시지 저장
                
                checkPacket(clientIP);
                
                
                
            }
        } finally {
            // Exception을 throw하여 외부에서 처리하도록 함.
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("TCP 소켓이 닫혔습니다.");
        }
    }
    
	public void checkPacket(String clientIP) {
		String receivedMessage = getLastReceivedMessage();
		System.out.println("Received ack Message: " + receivedMessage); // 받은 ack 출력

		// "_"를 기준으로 split하여 packetNum 추출
		String[] parts = receivedMessage.split("_");
		String packetString1 = parts[1].substring(0, 2);
		String packetString = packetString1.replaceAll("\\D", "");

		System.out.println("part [0]:" + parts[0] + ",part [1]:" + packetString);
		if (parts.length == 2) {
			int packetNum = Integer.parseInt(packetString);
			System.out.println("Part Num: " + packetNum);
			// 유효한 packetNum인지 확인하고 새 패킷이라면 처리
			synchronized (this) {
				if (packetNum > 0 && packetNum <= TOTAL_PACKETS && !packetReceived[packetNum]) {
					packetReceived[packetNum] = true; // 해당 패킷을 수신한 것으로 표시
					receivedPacketCount++; // 새 패킷 수신 시 count 증가
					//일시적으로 화면 append 코드 이동
	                receivedMessagesArea.append("[" + receive_message_num + "] 수신된 메시지 from " + clientIP + ": Ack Message" + receivedMessage + "\n");
	                System.out.println("수신된 메시지 from " + clientIP + ": " + receivedMessage);
					
					if (receivedPacketCount == TOTAL_PACKETS) {
						receivedPacketCount = 0;
						Arrays.fill(packetReceived, false);             
						newEchoReceived_tcp = true; // 에코 메시지를 받았을 경우
						System.out.println(" ---- newEchoMessage --- ");
						notifyAll();

					}
				}

				
				else if (packetNum > 0 && packetNum <= TOTAL_PACKETS && packetReceived[packetNum]) {
					
					newEchoReceived_tcp = false; // 에코 메시지를 받았을 경우
					System.out.println(" ---- old EchoMessage --- ");
					
				}
			}
		}
	}

}
