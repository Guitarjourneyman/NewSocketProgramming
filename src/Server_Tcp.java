import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JTextArea;

public class Server_Tcp {
    /* �޽����� �ޱ⸸ �ϴ� ��� ���� */
    private Socket socket;
    private static final int PORT = 8189;
    private JTextArea receivedMessagesArea;  // GUI�� receive message â
    private volatile boolean newAckReceived_tcp = false; // ���� �޽��� ���� ����
    private int receive_message_num = 0;

    public static byte[] checkNewMessage = new byte[2];
    // �����ڿ��� JTextArea ���� ����
    public Server_Tcp(Socket socket, JTextArea receivedMessagesArea) {
        this.socket = socket;
        this.receivedMessagesArea = receivedMessagesArea;
    }
    
    public void reset_message_num() {
        receive_message_num = 0;
    }
    
    public boolean hasNewEchoMessage() {
        return newAckReceived_tcp;
    }

    public void resetNewEchoMessageFlag() {
        newAckReceived_tcp = false;
    }

    public void startReceiving() throws IOException { //���� throw�Ͽ� ClientHandler���� ó���ϵ�����
        BufferedReader in = null;
        PrintWriter out = null;
        // ������ ������ ���� DataInputStream ���
        DataInputStream dataInputStream = null;
        try {
            // BufferedReader�� ����Ͽ� �����͸� �ۼ���
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            dataInputStream =new DataInputStream(socket.getInputStream());
            
            
            
            
            String clientIP = socket.getInetAddress().getHostAddress();
            System.out.println("Server_TCP is open");

            
            
            while (!socket.isClosed()) {//Ŭ���̾�Ʈ���� ������ �����ϸ鼭 �޽����� ���������� ����
                // ���ŵ� �޽��� ó��
            	// byte �迭 ����
                int byteArrayLength = dataInputStream.readInt(); // ���� ������ �迭�� ���̸� ����
                byte[] receivedData = new byte[byteArrayLength];
                dataInputStream.readFully(receivedData); // byte �迭 ����
                String receivedMessage = dataInputStream.readUTF();
              
                synchronized (this) {
                	checkNewMessage = receivedData;
                	//Ŭ���̾�Ʈ�κ��� ���� ����Ʈ�迭�� append 
                	StringBuilder temp = printByteArrayAsBinary(checkNewMessage);
                	receive_message_num++;
                    receivedMessagesArea.append("[" + receive_message_num + "] ���ŵ� �޽��� from " + clientIP + ": " +temp +receivedMessage +"\n" );
                    //newAckReceived_tcp = true; // ���� �޽����� �޾��� ���
                    //System.out.println("newAckMessage was coming");
                    //notifyAll();
                }
            }
        } finally {
            // Exception�� throw�Ͽ� �ܺο��� ó���ϵ��� ��.
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("TCP ������ �������ϴ�.");
        }
    }
    
    private StringBuilder printByteArrayAsBinary(byte[] byteArray) {
    	StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            // �� ����Ʈ�� 0�� 1�� ��ȯ
        	// 16���� 1111 1111�� &����
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.println(binaryString); // ��ȯ�� ������ ���
            binaryStringBuilder.append(binaryString).append(" ");
            
        }
        return binaryStringBuilder;
    }
}
