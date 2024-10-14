import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

public class ReceiverViewModelUdp {

    private static final int PORT = 1995;
    private static final int BUFFER_SIZE = 1024;
    private JTextArea receivedMessagesArea;  // GUI�� receive message â
    private static int receive_massage_num = 0;
    private volatile boolean newMessageReceived = false;
    
    // �����ڿ��� JTextArea ���� ����
    public ReceiverViewModelUdp(JTextArea receivedMessagesArea) {
        this.receivedMessagesArea = receivedMessagesArea;
    }
    
    public void reset_message_num() { // â�� clear �ϸ� meaage
    	receive_massage_num = 0;
    }
    
    public boolean hasNewMessage() {
        return newMessageReceived;
    }
    public void resetNewMessageFlag() {
    	newMessageReceived = false;
    }
    
    public void startServer() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Server " + " get started in Port."+PORT +" Waiting for Messages...");

            // ���� ������ �޽��� ��� ����
            while (true) {
                try {
                    // ���� ����
                    byte[] buffer = new byte[BUFFER_SIZE];

                    // ������ ��Ŷ ����
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    // ������ ����
                    socket.receive(receivePacket);

                    // ���ŵ� ������ ó��
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // Ŭ���̾�Ʈ IP �ּ� ��������
                    InetAddress clientAddress = receivePacket.getAddress();
                    String clientIP = clientAddress.getHostAddress();

                    // ���� �ð��� hh:mm:ss.SSS �������� ��������
                    String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());

                    // �޽����� �պκ� 20���ڸ� �߶� ǥ��
                    String truncatedMessage = receivedMessage.length() > 20
                            ? receivedMessage.substring(0, 20) + "..."
                            : receivedMessage;

                    // ���� �޽��� GUI�� ǥ��
                    receive_massage_num ++;
                    receivedMessagesArea.append("["+receive_massage_num+"]���ŵ� �޽��� from " + clientIP + ": " + truncatedMessage + " [" + timeStamp + "]\n");                    
                    System.out.println("I got Message : " + truncatedMessage );
                    
                    if(receivePacket != null) newMessageReceived = true; //�޽��� �� �޾��� ���
                    
                    
                } catch (Exception e) {
                    System.out.println("������ ���� �� ���� �߻�: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
    
    
}
