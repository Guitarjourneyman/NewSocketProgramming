import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JTextArea;

public class Server_Tcp {
	/*�޽����� �ޱ⸸ �ϴ� ��� ����*/
    private Socket socket;
    private static final int PORT = 8189;
    private JTextArea receivedMessagesArea;  // GUI�� receive message â
    private volatile boolean newEchoReceived_tcp = false; // ���� �޽��� ���� ����
    private int receive_message_num = 0;
    // �����ڿ��� JTextArea ���� ����
    public Server_Tcp(Socket socket, JTextArea receivedMessagesArea) {
        this.socket = socket;
        this.receivedMessagesArea = receivedMessagesArea;
    }
    
    public void reset_message_num() { // â�� clear �ϸ� meaage
    	receive_message_num = 0;
    }
    
    public boolean hasNewEchoMessage() {
        return newEchoReceived_tcp;
    }

    public void resetNewEchoMessageFlag() {
        newEchoReceived_tcp = false;
    }

    public void startReceiving() {
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
				receivedMessagesArea.append("["+receive_message_num+"]���ŵ� �޽��� from " + clientIP + ": " + receivedMessage + "\n");
				System.out.println("���ŵ� �޽��� from " + clientIP + ": " + receivedMessage);
				
				synchronized (this) {
					newEchoReceived_tcp = true; //���ο� ���ڸ޽����� �޾��� ���
					System.out.println("newEcho Received");
					notifyAll();
				}
				

				
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
                System.out.println("TCP ������ �������ϴ�.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
