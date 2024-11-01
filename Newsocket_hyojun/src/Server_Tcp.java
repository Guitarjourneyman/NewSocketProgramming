import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JTextArea;

public class Server_Tcp {
    /* �޽����� �ޱ⸸ �ϴ� ��� ���� */
    private Socket socket;
    private static final int PORT = 8189;
    private JTextArea receivedMessagesArea;  // GUI�� receive message â
    private volatile boolean newEchoReceived_tcp = false; // ���� �޽��� ���� ����
    private int receive_message_num = 0;
    private String lastReceivedMessage = ""; // �ֱ� ���ŵ� �޽��� ����
    
    // ��ü ��Ŷ ���� ���� ���θ� �����ϴ� �迭
    private static final int TOTAL_PACKETS = 61; // ��ü ��Ŷ �� (�ʿ信 �°� ����)
    private boolean[] packetReceived = new boolean[TOTAL_PACKETS + 1]; // �ε��� 0�� ������� ����
    private int receivedPacketCount = 0;
    
    // �����ڿ��� JTextArea ���� ����
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
    
    // �ֱ� ���ŵ� �޽��� ��ȯ �޼��� �߰�
    public String getLastReceivedMessage() {
        return lastReceivedMessage;
    }

    public void startReceiving() throws IOException { //���� throw�Ͽ� ClientHandler���� ó���ϵ�����
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            // BufferedReader�� ����Ͽ� �����͸� �ۼ���
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String clientIP = socket.getInetAddress().getHostAddress();
            System.out.println("Server_TCP is open");

            // Ŭ���̾�Ʈ���� ������ �����ϸ鼭 �޽����� ���������� ����
            String receivedMessage;
            while (!socket.isClosed() && (receivedMessage = in.readLine()) != null) {
                // ���ŵ� �޽��� ó��
            	
            	
                receive_message_num++;
                lastReceivedMessage = receivedMessage; // �ֱ� ���ŵ� �޽��� ����
                
                checkPacket(clientIP);
                
                
                
            }
        } finally {
            // Exception�� throw�Ͽ� �ܺο��� ó���ϵ��� ��.
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("TCP ������ �������ϴ�.");
        }
    }
    
	public void checkPacket(String clientIP) {
		String receivedMessage = getLastReceivedMessage();
		System.out.println("Received ack Message: " + receivedMessage); // ���� ack ���

		// "_"�� �������� split�Ͽ� packetNum ����
		String[] parts = receivedMessage.split("_");
		String packetString1 = parts[1].substring(0, 2);
		String packetString = packetString1.replaceAll("\\D", "");

		System.out.println("part [0]:" + parts[0] + ",part [1]:" + packetString);
		if (parts.length == 2) {
			int packetNum = Integer.parseInt(packetString);
			System.out.println("Part Num: " + packetNum);
			// ��ȿ�� packetNum���� Ȯ���ϰ� �� ��Ŷ�̶�� ó��
			synchronized (this) {
				if (packetNum > 0 && packetNum <= TOTAL_PACKETS && !packetReceived[packetNum]) {
					packetReceived[packetNum] = true; // �ش� ��Ŷ�� ������ ������ ǥ��
					receivedPacketCount++; // �� ��Ŷ ���� �� count ����
					//�Ͻ������� ȭ�� append �ڵ� �̵�
	                receivedMessagesArea.append("[" + receive_message_num + "] ���ŵ� �޽��� from " + clientIP + ": Ack Message" + receivedMessage + "\n");
	                System.out.println("���ŵ� �޽��� from " + clientIP + ": " + receivedMessage);
					
					if (receivedPacketCount == TOTAL_PACKETS) {
						receivedPacketCount = 0;
						Arrays.fill(packetReceived, false);             
						newEchoReceived_tcp = true; // ���� �޽����� �޾��� ���
						System.out.println(" ---- newEchoMessage --- ");
						notifyAll();

					}
				}

				
				else if (packetNum > 0 && packetNum <= TOTAL_PACKETS && packetReceived[packetNum]) {
					
					newEchoReceived_tcp = false; // ���� �޽����� �޾��� ���
					System.out.println(" ---- old EchoMessage --- ");
					
				}
			}
		}
	}

}
