import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

public class ReceiverViewModelUdp {

    private static final int PORT = 1996;
    private static final int BUFFER_SIZE = 1024;
    private JTextArea receivedMessagesArea;  // GUI�� receive message â
    private static int receive_message_num = 0;
    private volatile boolean newMessageReceived_udp = false;
    public int receivedMessageNum; 

    // �����ڿ��� JTextArea ���� ����
    public ReceiverViewModelUdp(JTextArea receivedMessagesArea) {
        this.receivedMessagesArea = receivedMessagesArea;
    }
    
    public void reset_message_num() {
        receive_message_num = 0;
    }
    
    public boolean hasNewMessage() {
        return newMessageReceived_udp;
    }
    
    public void resetNewMessageFlag() {
        newMessageReceived_udp = false;
    }
    public static String extractLeadingNumbers(String input) {
        // ���Խ��� ����� ���ڿ��� �պκп��� ���ڸ� ����
        StringBuilder numberStr = new StringBuilder();
        
        // ���ڿ��� �ϳ��� �˻��ؼ� ������ ��츸 numberStr�� �߰�
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                numberStr.append(c);
            } else {
                // ���ڰ� �ƴ� ���ڸ� ������ �ݺ��� �����ϰ� ���� �κи� ��ȯ
                break;
            }
        }
        
        // ���ڰ� ������ ���ڿ� ���·� ��ȯ�ϰ�, ������ null ��ȯ
        return numberStr.length() > 0 ? numberStr.toString() : null;
    }

    public void startServer() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Server started on port " + PORT + ". Waiting for messages...");

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

                    // �޽����� �պκ� 10���ڸ� �߶� ǥ��
                    String truncatedMessage = receivedMessage.length() > 10
                            ? receivedMessage.substring(0, 10)
                            : receivedMessage;

                    // ���� �޽��� GUI�� ǥ��
                    receive_message_num++;
                    
                    
                    receivedMessagesArea.append("[" + receive_message_num + "] Received UDP message from " + clientIP + ": " + truncatedMessage + " [" + timeStamp + "]\n");                    
                    System.out.println("I got Message: " + truncatedMessage);

                    // ���ڿ��� ������ ��ȯ
                    String numberStr = extractLeadingNumbers(truncatedMessage);
                    
                    if (numberStr != null) {
                        int number = Integer.parseInt(numberStr);
                        receivedMessageNum = number;
                        System.out.println("Extracted number: " + number);
                    } else {
                        System.out.println("No leading numbers found.");
                    }

                    if (receivePacket != null) {
                        newMessageReceived_udp = true; // ���ο� �޽����� �޾��� ���
                        System.out.println("newMessageReceived_udp set to true");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in received message: " + e.getMessage());
                } catch (SocketException e) {
                    System.out.println("Socket error occurred: " + e.getMessage());
                    break; // ���Ͽ� ������ ����� ������ Ż���Ͽ� ������ �ߴ��մϴ�.
                } catch (SecurityException e) {
                    System.out.println("Security exception: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal argument: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error while receiving data: " + e.getMessage());
                    e.printStackTrace(); // �߰����� ���� �α׸� ����Ͽ� ������ �� ��Ȯ�� �ľ��� �� �ְ� �մϴ�.
                }
            }
        } catch (SocketException e) {
            System.out.println("Failed to bind UDP socket to port " + PORT + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Server startup error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("UDP Server socket closed.");
            }
        }
    }
}
